package hanghae.api.coupteambe.controller;

import hanghae.api.coupteambe.domain.dto.ResResultDto;
import hanghae.api.coupteambe.domain.dto.kanban.BucketDto;
import hanghae.api.coupteambe.domain.dto.kanban.BucketInfoDto;
import hanghae.api.coupteambe.domain.dto.kanban.CardInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/buckets")
@RequiredArgsConstructor
public class KanbanController {

    /**
     * M2-1 버킷 생성
     */
    @PostMapping("/")
    public ResponseEntity<ResResultDto> createBuckets(@RequestBody BucketInfoDto bucketInfoDto) {

        // 반환값 : 결과 메시지, 상태값(201)
        return new ResponseEntity<>(new ResResultDto("버킷 생성 성공"), HttpStatus.CREATED);
    }

    /**
     * M2-2 버킷 수정
     */
    @PatchMapping("/")
    public ResponseEntity<ResResultDto> modifyBuckets(@RequestBody BucketInfoDto bucketInfoDto) {

        // 반환값 : 결과 메시지, 상태값(200)
        return ResponseEntity.ok(new ResResultDto("버킷 수정 성공"));
    }

    /**
     * M2-3 버킷 삭제
     */
    @DeleteMapping("/")
    public ResponseEntity<ResResultDto> deleteBuckets(@RequestParam("kbbId") String kbbId) {

        // 반환값 : 결과 메시지, 상태값(200)
        return ResponseEntity.ok(new ResResultDto("버킷 삭제 성공"));
    }

    /**
     * M2-4 전체 버킷, 카드 조회
     */
    @GetMapping("/")
    public ResponseEntity<List<BucketDto>> getAllBucketsAndCards(@RequestParam("pjId") String pjId) {
        List<BucketDto> bucketList = new ArrayList<>();

        // 반환값 : 버킷 & 카드 배열, 상태값(200)
        return ResponseEntity.ok(bucketList);
    }

    /**
     * M2-5 카드 생성
     */
    @PostMapping("/cards")
    public ResponseEntity<ResResultDto> createCards(@RequestBody CardInfoDto cardInfoDto) {

        // 반환값 : 결과 메시지, 상태값(201)
        return new ResponseEntity<>(new ResResultDto("카드 생성 성공"), HttpStatus.CREATED);
    }

    /**
     * M2-6 카드 수정
     */
    @PatchMapping("/cards")
    public ResponseEntity<ResResultDto> modifyCards(@RequestBody CardInfoDto cardInfoDto) {

        // 반환값 : 결과 메시지, 상태값(200)
        return ResponseEntity.ok(new ResResultDto("카드 수정 성공"));
    }

    /**
     * M2-7 카드 삭제
     */
    @DeleteMapping("/cards")
    public ResponseEntity<ResResultDto> deleteCards(@RequestParam("kbcId") String kbcId) {

        // 반환값 : 결과 메시지, 상태값(200)
        return ResponseEntity.ok(new ResResultDto("카드 삭제 성공"));
    }

    /**
     * M2-8 선택카드 상세 조회
     */
    @GetMapping("/cards")
    public ResponseEntity<List<CardInfoDto>> getOneCard(@RequestParam("kbcId") String kbcId) {
        List<CardInfoDto> cardInfo = new ArrayList<>();

        // 반환값 : 선택 카드 상세 정보, 상태값(200)
        return ResponseEntity.ok(cardInfo);
    }

//    /**
//     * M2-13 담당자별 카드 조회
//     */
//    @GetMapping("/cards")
//    public ResponseEntity<List<ManagerWorks>> getManagersWorks() {
//
//        List<ManagerWorks> managerWorks = new ArrayList<>();
//
//        // 반환값 : 담당자별 카드 정보, 상태값(200)
//        return ResponseEntity.ok(managerWorks);
//    }
}
