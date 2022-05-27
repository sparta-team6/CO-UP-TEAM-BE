package com.hanghae.coupteambe.api.controller;

import com.hanghae.coupteambe.api.domain.dto.ResResultDto;
import com.hanghae.coupteambe.api.domain.dto.notice.NoticeInfoDto;
import com.hanghae.coupteambe.api.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    /**
     * O1-1. 공지사항 글 생성
     */
    @PostMapping("/")
    public ResponseEntity<NoticeInfoDto> createNotices(@RequestBody NoticeInfoDto noticeInfoDto) {
        NoticeInfoDto resNotice = noticeService.createNotice(noticeInfoDto);
        return ResponseEntity.ok(resNotice);
    }

    /**
     * O1-2. 공지사항 전체 조회
     */
    @GetMapping("/all")
    public List<NoticeInfoDto> getAllNotices(@RequestParam("pjId") String pjId) {
        return noticeService.getAllNotices(pjId);
    }

    /**
     * O1-3. 선택 공지사항 조회
     */
    @GetMapping("/")
    public ResponseEntity<NoticeInfoDto> getOneNotice(@RequestParam("noticeId") String noticeId) {
        NoticeInfoDto noticeInfoDto = noticeService.getOneNotice(noticeId);
        return ResponseEntity.ok(noticeInfoDto);
    }

    /**
     * O1-4. 공지사항 글 수정
     */
    @PatchMapping("/")
    public ResponseEntity<ResResultDto> modifyNotice(@RequestBody NoticeInfoDto noticeInfoDto) {
        noticeService.modifyNotice(noticeInfoDto);
        return ResponseEntity.ok(new ResResultDto("공지사항 수정 성공"));
    }

    /**
     * O1-5. 공지사항 글 삭제
     */
    @DeleteMapping("/")
    public ResponseEntity<ResResultDto> deleteNotice(@RequestBody NoticeInfoDto noticeInfoDto) {
        noticeService.deleteNotice(noticeInfoDto);
        return ResponseEntity.ok(new ResResultDto("공지사항 삭제 성공"));
    }

}
