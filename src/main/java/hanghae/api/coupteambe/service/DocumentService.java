package hanghae.api.coupteambe.service;

import hanghae.api.coupteambe.domain.dto.document.DocumentDto;
import hanghae.api.coupteambe.domain.entity.document.Folder;
import hanghae.api.coupteambe.domain.repository.document.DocumentFolderRepository;
import hanghae.api.coupteambe.domain.repository.document.DocumentRepository;
import hanghae.api.coupteambe.util.exception.ErrorCode;
import hanghae.api.coupteambe.util.exception.RequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import hanghae.api.coupteambe.domain.entity.document.Document;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentFolderRepository documentFolderRepository;

    /**
     * M1-5 문서생성
     */
    @Transactional
    public void createDocuments(DocumentDto documentDto) {

        // 1. 파라미터로 받은 문서 객체에서 필요한 데이터를 추출한다.
        String folderId = documentDto.getDfId();
        Optional<Folder> optionalFolder = documentFolderRepository.findById(UUID.fromString(folderId));
        Folder folder = optionalFolder.orElseThrow(
                () -> new RequestException(ErrorCode.FOLDER_NOT_FOUND_404));

        // 2. 새 문서 객체를 생성한다.
        Document document = Document.builder()
                                    .folder(folder)
                                    .title(documentDto.getTitle())
                                    .contents(documentDto.getContents())
                                    .position(documentDto.getPosition())
                                    .build();

        // 3. 새로 생성한 객체를 Repository 를 이용하여 DB에 저장한다
        documentRepository.save(document);
    }

    /**
     * M1-6 문서수정
     */
    @Transactional
    public void modifyDocument(DocumentDto documentDto) {

        // 1. 파리미터로 받은 문서 객체에서 필요한 데이터를 추출한다
        String documentId = documentDto.getDocId();

        // 2. 문서 ID를 key로 해당 문서를 DB에서 조회한다.
        Optional<Document> optionalDocument = documentRepository.findById(UUID.fromString(documentId));
        Document document = optionalDocument.orElseThrow(
                () -> new RequestException(ErrorCode.DOCUMENT_NOT_FOUND_404));

        // 3. 새 문서 객체를 생성하고 조회한 문서로 초기화 한다.
        // 4. 문서를 업데이트한다.
        document.updateDocument(documentDto);
    }

    /**
     * M1-7 문서삭제
     */
    @Transactional
    public void deleteDocument(String docId) {

        // 파라미터로 받은 문서 ID를 key 로 DB에서 해당 카드를 삭제한다.
        // Repository(JPA)이용
        documentRepository.deleteById(UUID.fromString(docId));
    }

    /**
     * M1-8 선택 문서 상세 조회
     */
    @Transactional
    public DocumentDto getOneDocument(String docId) {

        // 1. 파라미터로 받은 문서 ID를 key로 문서 정보를 조회한다.
        Optional<Document> optionalDocument = documentRepository.findById(UUID.fromString(docId));
        Document document = optionalDocument.orElseThrow(
                () -> new RequestException(ErrorCode.DOCUMENT_NOT_FOUND_404));

        // 2. 새 문서 객체를 생성하고 조회한 문서로 초기화한다.
        // 3. 생성된 문서 객체를 반환한다.

        return new DocumentDto(document);

        /// TODO: 2022/05/12 테스트해보자
    }
}
