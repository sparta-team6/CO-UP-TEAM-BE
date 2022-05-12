package hanghae.api.coupteambe.service;

import hanghae.api.coupteambe.domain.dto.kanban.BucketDto;
import hanghae.api.coupteambe.domain.dto.kanban.BucketInfoDto;
import hanghae.api.coupteambe.domain.dto.kanban.CardInfoDto;
import hanghae.api.coupteambe.domain.entity.kanban.KanbanBucket;
import hanghae.api.coupteambe.domain.entity.kanban.KanbanCard;
import hanghae.api.coupteambe.domain.entity.project.Project;
import hanghae.api.coupteambe.domain.repository.kanban.KanbanBucketRepository;
import hanghae.api.coupteambe.domain.repository.kanban.KanbanCardRepository;
import hanghae.api.coupteambe.domain.repository.project.ProjectRepository;
import hanghae.api.coupteambe.util.exception.ErrorCode;
import hanghae.api.coupteambe.util.exception.RequestException;
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
        String projectId = bucketInfoDto.getPjId();
        Optional<Project> optionalProject = projectRepository.findById(UUID.fromString(projectId));
        Project project = optionalProject.orElseThrow(() -> new RequestException(ErrorCode.PROJECT_NOT_FOUND_404));

        // 2. 새 버킷 객체를 생성한다.
        KanbanBucket newBucket = KanbanBucket.builder()
                                             .project(project)
                                             .title(bucketInfoDto.getTitle())
                                             .position(bucketInfoDto.getPosition())
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
        String bucketId = bucketInfoDto.getKbbId();

        // 2. 버킷 ID를 key 로 해당 버킷을 DB 에서 조회한다. Repository(JPA)이용
        Optional<KanbanBucket> optionalKanbanBucket = kanbanBucketRepository.findById(UUID.fromString(bucketId));
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

        // 파라매터로 받은 버킷 ID를 key 로 DB 에서 해당 버킷을 삭제한다.
        kanbanBucketRepository.deleteById(UUID.fromString(kbbId));

    }

    /**
     * M2-4 전체 버킷, 카드 조회
     */
    @Transactional
    public List<BucketDto> getAllBucketsAndCards(String projectId) {

        // 1. 파라매터로 받은 프로젝트 ID 를 가지고 있는 모든 버킷들을 조회한다.

        // 2. 순차적으로 조회된 버킷의 버킷 ID를 가지고 있는 모든 카드들을 조회한다.
        List<KanbanBucket> buckets = kanbanBucketRepository.findBucketsAndCardsByProject_Id_DSL(projectId);
        List<BucketDto> bucketDtos = new ArrayList<>();

        buckets.forEach(kanbanBucket -> {
            List<CardInfoDto> cardDtos = new ArrayList<>();
            List<KanbanCard> cards = kanbanBucket.getCards();
            cards.forEach(kanbanCard -> {
                cardDtos.add(new CardInfoDto(kanbanCard));
            });
            bucketDtos.add(BucketDto.builder()
                                    .kbbId(kanbanBucket.getId().toString())
                                    .title(kanbanBucket.getTitle())
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
        String bucketId = cardInfoDto.getKbbId();
        Optional<KanbanBucket> optionalBucket = kanbanBucketRepository.findById(UUID.fromString(bucketId));
        KanbanBucket kanbanBucket = optionalBucket.orElseThrow(
                () -> new RequestException(ErrorCode.KANBAN_BUCKET_NOT_FOUND_404));

        // 2. 새 카드 객체를 생성한다.
        KanbanCard kanbanCard = KanbanCard.builder()
                                          .kanbanBucket(kanbanBucket)
                                          .manager(cardInfoDto.getManager())
                                          .title(cardInfoDto.getTitle())
                                          .contents(cardInfoDto.getContents())
                                          .position(cardInfoDto.getPosition()).build();


        // 3. 새로 생성한 카드를 Repository 를 이용하여 DB에 저장한다.
        kanbanCardRepository.save(kanbanCard);

    }

    /**
     * M2-6 카드 수정
     */
    @Transactional
    public void modifyCard(CardInfoDto cardInfoDto) {

        // 1. 파라매터로 받은 카드 객체에서 필요한 데이터를 추출한다.
        String cardId = cardInfoDto.getKbcId();

        // 2. 카드 ID를 key 로 해당 카드를 DB 에서 조회한다. Repository(JPA)이용
        Optional<KanbanCard> optionalKanbanCard = kanbanCardRepository.findById(UUID.fromString(cardId));
        KanbanCard kanbanCard = optionalKanbanCard.orElseThrow(
                () -> new RequestException(ErrorCode.KANBAN_CARD_NOT_FOUND_404));

        // 3. 새 카드 객체를 생성하고 조회한 카드로 초기화 한다.
        // 4. 카드를 업데이트 시킨다.
        kanbanCard.updateKanbanCard(cardInfoDto);

    }

    /**
     * M2-7 카드 삭제
     */
    @Transactional
    public void deleteCard(String kbcId) {

        // 파라매터로 받은 카드 ID를 key 로 DB 에서 해당 카드를 삭제한다.
        // Repository(JPA)이용
        kanbanCardRepository.deleteById(UUID.fromString(kbcId));

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

}
