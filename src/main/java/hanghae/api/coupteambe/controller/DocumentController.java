package hanghae.api.coupteambe.controller;

import hanghae.api.coupteambe.domain.dto.ResResultDto;
import hanghae.api.coupteambe.domain.dto.document.DocumentDto;
import hanghae.api.coupteambe.domain.dto.document.FolderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/folders")
@RequiredArgsConstructor
public class DocumentController {

    /**
     * M1-1 폴더 생성
     */
    @PostMapping("/")
    public ResponseEntity<ResResultDto> postFolder(@RequestBody FolderDto folderDto) {
        // 반환값 : 결과 메시지, 상태값(201)
        return new ResponseEntity<>(new ResResultDto("폴더 생성 성공"), HttpStatus.CREATED);
    }

    /**
     * M1-2 폴더 수정
     */
    @PatchMapping("/{dfId}")
    public ResponseEntity<ResResultDto> patchFolder(@RequestBody FolderDto folderDto) {
        // 반환값 : 결과 메시지, 상태값(200)
        return ResponseEntity.ok(new ResResultDto("폴더 수정 성공"));
    }

    /**
     * M1-3 폴더 삭제
     */
    @DeleteMapping("/{dfId}")
    public ResponseEntity<ResResultDto> deleteFolder(@RequestParam("dfId") String dfId) {
        // 반환값 : 결과 메시지, 상태값(200)
        return ResponseEntity.ok(new ResResultDto("폴더 삭제 성공"));
    }

    /**
     * M1-4 폴더내의 문서 제목 조회
     */
    @GetMapping("/")
    public ResponseEntity<List<FolderDto>> getFolders() {
        List<FolderDto> folderDtos = new ArrayList<>();
        // 반환값 : 폴더내의 문서 제목값, 상태값(200)
        return ResponseEntity.ok(folderDtos);
    }

    /**
     * M1-5 문서생성
     */
    @PostMapping("/docs")
    public ResponseEntity<ResResultDto> postDoc(@RequestBody DocumentDto documentDto) {
        // 반환값 : 결과 메시지, 상태값(201)
        return new ResponseEntity<>(new ResResultDto("문서 생성 성공"), HttpStatus.CREATED);
    }

    /**
     * M1-6 문서수정
     */
    @PatchMapping("/docs/{docId}")
    public ResponseEntity<ResResultDto> patchDoc(@RequestBody DocumentDto documentDto) {
        // 반환값 : 결과 메시지, 상태값(200)
        return ResponseEntity.ok(new ResResultDto("문서 수정 성공"));
    }

    /**
     * M1-7 문서삭제
     */
    @DeleteMapping("/docs/{docId}")
    public ResponseEntity<ResResultDto> deleteDoc(@RequestParam("docId") String docId) {
        // 반환값 : 결과 메시지, 상태값(200)
        return ResponseEntity.ok(new ResResultDto("문서 삭제 성공"));
    }

    /**
     * M1-8 선택 문서 상세 조회
     */
    @GetMapping("/docs")
    public ResponseEntity<DocumentDto> getOneDoc(@RequestBody DocumentDto documentDto) {
        // 반환값 : 선택 문서 상세 조회값, 상태값(200)
        return ResponseEntity.ok(documentDto);
    }


}
