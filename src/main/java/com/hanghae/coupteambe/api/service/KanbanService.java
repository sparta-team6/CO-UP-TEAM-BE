package com.hanghae.coupteambe.api.service;

import com.hanghae.coupteambe.api.domain.dto.kanban.BucketDto;
import com.hanghae.coupteambe.api.domain.dto.kanban.BucketInfoDto;
import com.hanghae.coupteambe.api.domain.dto.kanban.CardInfoDto;
import com.hanghae.coupteambe.api.domain.dto.kanban.ManagerBucketCardsDto;
import com.hanghae.coupteambe.api.domain.entity.kanban.KanbanBucket;
import com.hanghae.coupteambe.api.domain.entity.kanban.KanbanCard;
import com.hanghae.coupteambe.api.domain.entity.project.Project;
import com.hanghae.coupteambe.api.domain.repository.kanban.KanbanBucketRepository;
import com.hanghae.coupteambe.api.domain.repository.kanban.KanbanCardRepository;
import com.hanghae.coupteambe.api.domain.repository.project.ProjectRepository;
import com.hanghae.coupteambe.api.enumerate.StatusFlag;
import com.hanghae.coupteambe.api.util.exception.ErrorCode;
import com.hanghae.coupteambe.api.util.exception.RequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KanbanService {

    private final KanbanBucketRepository kanbanBucketRepository;
    private final KanbanCardRepository kanbanCardRepository;
    private final ProjectRepository projectRepository;

    /**
     * M2-1 버킷 생성
     */
    @Transactional
    public void createBucket(BucketInfoDto bucketInfoDto) {

        // 1. 파라매터로 받은 버킷 객체에서 필요한 데이터를 추출한다.
        UUID projectId = bucketInfoDto.getPjId();
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        Project project = optionalProject.orElseThrow(() -> new RequestException(ErrorCode.PROJECT_NOT_FOUND_404));

        // 카운팅
        long cntBuckets = kanbanBucketRepository.countAllByproject_IdAndDelFlag(projectId, StatusFlag.NORMAL);

        // 2. 새 버킷 객체를 생성한다.
        KanbanBucket newBucket = KanbanBucket.builder()
                                             .project(project)
                                             .title(bucketInfoDto.getTitle())
                                             .position((int) cntBuckets)
                                             .build();

        // 3. 새로 생성한 버킷을 Repository 를 이용하여 DB에 저장한다.
        kanbanBucketRepository.save(newBucket);
    }

    /**
     * M2-2 버킷 수정
     */
    @Transactional
    public void modifyBucket(BucketInfoDto bucketInfoDto) {

        // 1. 파라매터로 받은 버킷 객체에서 필요한 데이터를 추출한다.
        UUID bucketId = bucketInfoDto.getKbbId();

        // 2. 버킷 ID를 key 로 해당 버킷을 DB 에서 조회한다. Repository(JPA)이용
        Optional<KanbanBucket> optionalKanbanBucket = kanbanBucketRepository.findById(bucketId);
        KanbanBucket kanbanBucket = optionalKanbanBucket.orElseThrow(
                () -> new RequestException(ErrorCode.KANBAN_BUCKET_NOT_FOUND_404));

        // 3. 새 버킷 객체를 생성하고 조회한 버킷으로 초기화 한다.
        // 4. 버킷을 업데이트 시킨다.
        kanbanBucket.updateBucket(bucketInfoDto);
    }

    /**
     * M2-3 버킷 삭제
     */
    @Transactional
    public void deleteBucket(String kbbId) {

        Optional<KanbanBucket> optionalKanbanBucket = kanbanBucketRepository.findById(UUID.fromString(kbbId));
        KanbanBucket kanbanBucket = optionalKanbanBucket.orElseThrow(
                () -> new RequestException(ErrorCode.KANBAN_BUCKET_NOT_FOUND_404));

        kanbanBucket.delete();
    }

    /**
     * M2-4 전체 버킷, 카드 조회
     */
    @Transactional
    public List<BucketDto> getAllBucketsAndCards(String projectId) {

        // 1. 파라매터로 받은 프로젝트 ID 를 가지고 있는 모든 버킷들을 조회한다.

        // 2. 순차적으로 조회된 버킷의 버킷 ID를 가지고 있는 모든 카드들을 조회한다.
        List<KanbanBucket> buckets = kanbanBucketRepository.findBucketsByProject_Id_DSL(projectId);
        List<BucketDto> bucketDtos = new ArrayList<>();

        buckets.forEach(kanbanBucket -> {
            List<CardInfoDto> cardDtos = new ArrayList<>();
            List<KanbanCard> cards = kanbanCardRepository.findCardsByKanbanBucket_Id_DSL(kanbanBucket.getId());
            cards.forEach(kanbanCard -> {
                cardDtos.add(new CardInfoDto(kanbanCard));
            });
            bucketDtos.add(BucketDto.builder()
                                    .kbbId(kanbanBucket.getId())
                                    .title(kanbanBucket.getTitle())
                                    .position(kanbanBucket.getPosition())
                                    .cards(cardDtos).build());
        });

        return bucketDtos;
    }

    /**
     * M2-5 카드 생성
     */
    @Transactional
    public void createCard(CardInfoDto cardInfoDto) {

        // 1. 파라매터로 받은 카드 객체에서 필요한 데이터를 추출한다.
        UUID bucketId = cardInfoDto.getKbbId();
        Optional<KanbanBucket> optionalBucket = kanbanBucketRepository.findById(bucketId);
        KanbanBucket kanbanBucket = optionalBucket.orElseThrow(
                () -> new RequestException(ErrorCode.KANBAN_BUCKET_NOT_FOUND_404));

        long cntCards = kanbanCardRepository.countAllByKanbanBucket_IdAndDelFlag(bucketId, StatusFlag.NORMAL);

        // 2. 새 카드 객체를 생성한다.
        KanbanCard kanbanCard = KanbanCard.builder()
                                          .kanbanBucket(kanbanBucket)
                                          .manager(cardInfoDto.getManager())
                                          .managerNickname(cardInfoDto.getManagerNickname())
                                          .title(cardInfoDto.getTitle())
                                          .contents(cardInfoDto.getContents())
                                          .position((int) cntCards).build();


        // 3. 새로 생성한 카드를 Repository 를 이용하여 DB에 저장한다.
        kanbanCardRepository.save(kanbanCard);

    }

    /**
     * M2-6 카드 수정
     */
    @Transactional
    public void modifyCard(CardInfoDto nextCardInfoDto) {

        Optional<KanbanCard> optionalKanbanCard = kanbanCardRepository
                .findByIdAndDelFlag(nextCardInfoDto.getKbcId(), StatusFlag.NORMAL);
        KanbanCard currentCard = optionalKanbanCard.orElseThrow(
                () -> new RequestException(ErrorCode.KANBAN_CARD_NOT_FOUND_404));

        kanbanCardRepository.decreaseCardsPosition(currentCard.getKanbanBucket().getId(), currentCard.getPosition());
        kanbanCardRepository.increaseCardsPosition(nextCardInfoDto.getKbbId(), nextCardInfoDto.getPosition());

        UUID dstBucketId = nextCardInfoDto.getKbbId();
        Optional<KanbanBucket> optTargetBucket = kanbanBucketRepository.findByIdAndDelFlag(dstBucketId,
                StatusFlag.NORMAL);
        KanbanBucket dstBucket = optTargetBucket.orElseThrow(
                () -> new RequestException(ErrorCode.KANBAN_BUCKET_NOT_FOUND_404));

        currentCard.updateKanbanCard(nextCardInfoDto);
        currentCard.updateBucket(dstBucket);
    }

    /**
     * M2-7 카드 삭제
     */
    @Transactional
    public void deleteCard(String kbcId) {

        Optional<KanbanCard> optionalKanbanCard = kanbanCardRepository.findById(UUID.fromString(kbcId));
        KanbanCard targetCard = optionalKanbanCard.orElseThrow(
                () -> new RequestException(ErrorCode.KANBAN_CARD_NOT_FOUND_404));

        kanbanCardRepository.decreaseCardsPosition(targetCard.getKanbanBucket().getId(), targetCard.getPosition());

        targetCard.delete();

    }

    /**
     * M2-8 선택카드 상세 조회
     */
    @Transactional
    public CardInfoDto getOneCard(String kbcId) {

        // 1. 파라매터로 받은 카드 ID 를 key 로 카드 정보를 조회한다.
        Optional<KanbanCard> optionalKanbanCard = kanbanCardRepository.findById(UUID.fromString(kbcId));
        KanbanCard kanbanCard = optionalKanbanCard.orElseThrow(
                () -> new RequestException(ErrorCode.KANBAN_CARD_NOT_FOUND_404));

        // 2. 새 카드 객체를 생성하고 조회한 카드로 초기화 한다.
        // 3. 생성된 카드 객체를 반환한다.

        return new CardInfoDto(kanbanCard);
    }

    /**
     * M2-13 담당자별 카드 조회
     */
    @Transactional
    public List<ManagerBucketCardsDto> getAllManagersBucketsAndCards(String projectId) {

        // 1. 파라매터로 받은 프로젝트 ID 를 가지고 있는 모든 버킷들을 조회한다.

        // 2. 순차적으로 조회된 버킷의 버킷 ID를 가지고 있는 모든 카드들을 조회한다.

        return kanbanBucketRepository.findManagersBucketsByProject_Id_DSL(projectId);
    }

    //todo 추후 모든카드 수정시 작성
//    @Transactional
//    public void updateBucketAllCards(List<BucketDto> bucketDtoList) {
//
//
//    }
}
