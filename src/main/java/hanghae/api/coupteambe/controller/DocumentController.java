package hanghae.api.coupteambe.controller;

import hanghae.api.coupteambe.domain.dto.ResResultDto;
import hanghae.api.coupteambe.domain.entity.document.DocumentDto;
import hanghae.api.coupteambe.domain.entity.document.FolderDto;
import hanghae.api.coupteambe.service.DocumentService;
import hanghae.api.coupteambe.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/folders")
@RequiredArgsConstructor
public class DocumentController {

    private final FolderService folderService;
    private final DocumentService documentService;

    /**
     * M1-1 폴더 생성
     */
    @PostMapping("/")
    public ResponseEntity<ResResultDto> postFolders(@RequestBody FolderDto folderDto) {

        isValidMember();
        // 폴더 생성 서비스 호출
        folderService.createFolders(folderDto);

        // 반환값 : 결과 메시지, 상태값(201)
        return new ResponseEntity<>(new ResResultDto("폴더 생성 성공"), HttpStatus.CREATED);
    }

    /**
     * M1-2 폴더 수정
     */
    @PatchMapping("/")
    public ResponseEntity<ResResultDto> modifyFolders(@RequestBody FolderDto folderDto) {

        // 폴더 수정 서비스 호출
        folderService.modifyFolders(folderDto);

        // 반환값 : 결과 메시지, 상태값(200)
        return ResponseEntity.ok(new ResResultDto("폴더 수정 성공"));
    }

    /**
     * M1-3 폴더 삭제
     */
    @DeleteMapping("/")
    public ResponseEntity<ResResultDto> deleteFolders(@RequestParam("dfId") String dfId) {

        // 폴더 삭제 서비스 호출
        folderService.deleteFolders(dfId);

        // 반환값 : 결과 메시지, 상태값(200)
        return ResponseEntity.ok(new ResResultDto("폴더 삭제 성공"));
    }

    /**
     * M1-4 전체 폴더, 문서 조회
     */
    @GetMapping("/")
    public ResponseEntity<List<FolderDto>> getAllFoldersAndDocuments(@RequestParam("pjId") String pjId) {

        // 전체 폴더, 문서 조회 서비스 호출
        List<FolderDto> folderList = folderService.getAllFoldersAndDocuments(pjId);

        // 반환값 : 폴더내의 문서 제목값, 상태값(200)
        return ResponseEntity.ok(folderList);
    }

    /**
     * M1-5 문서생성
     */
    @PostMapping("/docs")
    public ResponseEntity<ResResultDto> createDocuments(@RequestBody DocumentDto documentDto) {

        // 카드 생성 서비스 호출
        documentService.createDocuments(documentDto);

        // 반환값 : 결과 메시지, 상태값(201)
        return new ResponseEntity<>(new ResResultDto("문서 생성 성공"), HttpStatus.CREATED);
    }

    /**
     * M1-6 문서수정
     */
    @PatchMapping("/docs/")
    public ResponseEntity<ResResultDto> modifyDocument(@RequestBody DocumentDto documentDto) {

        // 문서 수정 서비스 호출
        documentService.modifyDocument(documentDto);

        // 반환값 : 결과 메시지, 상태값(200)
        return ResponseEntity.ok(new ResResultDto("문서 수정 성공"));
    }

    /**
     * M1-7 문서삭제
     */
    @DeleteMapping("/docs/")
    public ResponseEntity<ResResultDto> deleteDoc(@RequestParam("docId") String docId) {

        // 문서 삭제 서비스 호출
        documentService.deleteDocument(docId);
        // 반환값 : 결과 메시지, 상태값(200)
        return ResponseEntity.ok(new ResResultDto("문서 삭제 성공"));
    }

    /**
     * M1-8 선택 문서 상세 조회
     */
    @GetMapping("/docs")
    public ResponseEntity<DocumentDto> getOneDoc(@RequestParam("docId") String docId) {

        // 선택문서 상세 조회 서비스 호출
        DocumentDto documentDto = documentService.getOneDocument(docId);
        // 반환값 : 선택 문서 상세 조회값, 상태값(200)
        return ResponseEntity.ok(documentDto);
    }

    private boolean isValidMember() {

        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();

        //todo 프로젝트에 참가한 멤버인지 확인 로직 작성해야함
        // 권한이 없는 멤버라면 '프로젝트 권한이 없습니다' 예외 처리.

        return true;
    }

}
