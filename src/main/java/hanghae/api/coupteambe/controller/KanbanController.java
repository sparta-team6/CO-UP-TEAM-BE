package hanghae.api.coupteambe.controller;

import hanghae.api.coupteambe.domain.dto.ResResultDto;
import hanghae.api.coupteambe.domain.dto.kanban.BucketDto;
import hanghae.api.coupteambe.domain.dto.kanban.BucketInfoDto;
import hanghae.api.coupteambe.domain.dto.kanban.CardInfoDto;
import hanghae.api.coupteambe.domain.dto.kanban.ManagerBucketCardsDto;
import hanghae.api.coupteambe.service.KanbanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buckets")
@RequiredArgsConstructor
public class KanbanController {

    //todo 1차 구현물은 버킷에 대한 객체들이 적합한 사용자의 요청을 처리하는지 검증하는 과정이 없다.
    // 즉, 프로젝트에 참가하지 않은 사용자가 임의로 버킷내용을 조작할 수 있는 상태이다.
    // 그러므로, 2차 구현물에서는 컨트롤러에서 프로젝트에 참가한 사용자인지 검증하는 과정이 추가되어야한다.

    private final KanbanService kanbanService;

    /**
     * M2-1 버킷 생성
     */
    @PostMapping("/")
    public ResponseEntity<ResResultDto> createBuckets(@RequestBody BucketInfoDto bucketInfoDto) {

        isValidMember();
        // 버킷 생성 서비스 호출
        kanbanService.createBucket(bucketInfoDto);

        // 반환값 : 결과 메시지, 상태값(201)
        return new ResponseEntity<>(new ResResultDto("버킷 생성 성공"), HttpStatus.CREATED);
    }

    /**
     * M2-2 버킷 수정
     */
    @PatchMapping("/")
    public ResponseEntity<ResResultDto> modifyBuckets(@RequestBody BucketInfoDto bucketInfoDto) {

        // 버킷 수정 서비스 호출
        kanbanService.modifyBucket(bucketInfoDto);

        // 반환값 : 결과 메시지, 상태값(200)
        return ResponseEntity.ok(new ResResultDto("버킷 수정 성공"));
    }

    /**
     * M2-3 버킷 삭제
     */
    @DeleteMapping("/")
    public ResponseEntity<ResResultDto> deleteBuckets(@RequestParam("kbbId") String kbbId) {

        // 버킷 삭제 서비스 호출
        kanbanService.deleteBucket(kbbId);

        // 반환값 : 결과 메시지, 상태값(200)
        return ResponseEntity.ok(new ResResultDto("버킷 삭제 성공"));
    }

    /**
     * M2-4 전체 버킷, 카드 조회
     */
    @GetMapping("/")
    public ResponseEntity<List<BucketDto>> getAllBucketsAndCards(@RequestParam("pjId") String pjId) {

        // 전체 버킷, 카드 조회 서비스 호출
        List<BucketDto> bucketList = kanbanService.getAllBucketsAndCards(pjId);

        // 반환값 : 버킷 & 카드 배열, 상태값(200)
        return ResponseEntity.ok(bucketList);
    }

    /**
     * M2-5 카드 생성
     */
    @PostMapping("/cards")
    public ResponseEntity<ResResultDto> createCards(@RequestBody CardInfoDto cardInfoDto) {

        // 카드 생성 서비스 호출
        kanbanService.createCard(cardInfoDto);

        // 반환값 : 결과 메시지, 상태값(201)
        return new ResponseEntity<>(new ResResultDto("카드 생성 성공"), HttpStatus.CREATED);
    }

    /**
     * M2-6 카드 수정
     */
    @PatchMapping("/cards")
    public ResponseEntity<ResResultDto> modifyCards(@RequestBody CardInfoDto cardInfoDto) {

        // 카드 수정 서비스 호출
        kanbanService.modifyCard(cardInfoDto);

        // 반환값 : 결과 메시지, 상태값(200)
        return ResponseEntity.ok(new ResResultDto("카드 수정 성공"));
    }

    /**
     * M2-7 카드 삭제
     */
    @DeleteMapping("/cards")
    public ResponseEntity<ResResultDto> deleteCards(@RequestParam("kbcId") String kbcId) {

        // 카드 삭제 서비스 호출
        kanbanService.deleteCard(kbcId);

        // 반환값 : 결과 메시지, 상태값(200)
        return ResponseEntity.ok(new ResResultDto("카드 삭제 성공"));
    }

    /**
     * M2-8 선택카드 상세 조회
     */
    @GetMapping("/cards")
    public ResponseEntity<CardInfoDto> getOneCard(@RequestParam("kbcId") String kbcId) {

        // 선택카드 상세 조회 서비스 호출
        CardInfoDto cardInfo = kanbanService.getOneCard(kbcId);

        // 반환값 : 선택 카드 상세 정보, 상태값(200)
        return ResponseEntity.ok(cardInfo);
    }

    /**
     * M2-13 담당자별 카드 조회
     */
    @GetMapping("/cards/managers")
    public ResponseEntity<List<ManagerBucketCardsDto>> getAllManagersBucketsAndCards(
            @RequestParam("pjId") String pjId) {

        List<ManagerBucketCardsDto> ManagerBucketCardsDto = kanbanService.getAllManagersBucketsAndCards(pjId);

        // 반환값 : 담당자별 카드 정보, 상태값(200)
        return ResponseEntity.ok(ManagerBucketCardsDto);
    }

    /**
     * M2-14 전체 버킷,카드 수정
     */
    //xxx 잠시 보류, 한장의 카드만 수정하는 것으로 고려중, 성공한다면 이 메소드는 필요없음
//    @PatchMapping("/cards/all")
//    public ResponseEntity<ResResultDto> updateAllBucketsAndCards(@RequestBody List<BucketDto> bucketDtoList) {
//
//        for (BucketDto bucketDto : bucketDtoList) {
//            System.out.println("bucketDto = " + bucketDto);
//            List<CardInfoDto> cards = bucketDto.getCards();
//            for (CardInfoDto card : cards) {
//                System.out.println("card = " + card);
//            }
//        }
//
//        kanbanService.updateBucketAllCards(bucketDtoList);
//
//
//        return ResponseEntity.ok(new ResResultDto("전체 카드 수정 성공"));
//    }
    private boolean isValidMember() {

        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();

        //todo 프로젝트에 참가한 멤버인지 확인 로직 작성해야함
        // 권한이 없는 멤버라면 '프로젝트 권한이 없습니다' 예외 처리.

        return true;
    }
}
