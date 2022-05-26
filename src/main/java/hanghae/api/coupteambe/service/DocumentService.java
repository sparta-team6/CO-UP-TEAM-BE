package hanghae.api.coupteambe.service;

import hanghae.api.coupteambe.domain.dto.document.DocumentDto;
import hanghae.api.coupteambe.domain.entity.document.Document;
import hanghae.api.coupteambe.domain.entity.document.Folder;
import hanghae.api.coupteambe.domain.entity.member.Member;
import hanghae.api.coupteambe.domain.entity.project.Project;
import hanghae.api.coupteambe.domain.entity.project.ProjectMember;
import hanghae.api.coupteambe.domain.repository.document.DocumentFolderRepository;
import hanghae.api.coupteambe.domain.repository.document.DocumentRepository;
import hanghae.api.coupteambe.domain.repository.member.MemberRepository;
import hanghae.api.coupteambe.domain.repository.project.ProjectMemberRepository;
import hanghae.api.coupteambe.domain.repository.project.ProjectRepository;
import hanghae.api.coupteambe.enumerate.StatusFlag;
import hanghae.api.coupteambe.util.exception.ErrorCode;
import hanghae.api.coupteambe.util.exception.RequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static hanghae.api.coupteambe.util.SecurityUtil.getCurrentUsername;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentFolderRepository documentFolderRepository;
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    /**
     * M1-5 문서생성
     */
    @Transactional
    public DocumentDto createDocuments(DocumentDto documentDto) {
        // 1. 로그인 한 유저의 로그인 ID 추출
        String loginId = getCurrentUsername()
                // 1-1. 로그인 안된 경우 예외처리
                .orElseThrow(() -> new RequestException(ErrorCode.COMMON_BAD_REQUEST_400));

        // 2. DB 에 해당 유저가 존재하는지 확인
        Member member = memberRepository.findByLoginId(loginId)
                // 2-1. 존재하지 않는 경우 예외처리
                .orElseThrow(() -> new RequestException(ErrorCode.MEMBER_LOGINID_NOT_FOUND_404));

        // 3. DB 에 해당 문서를 저장할 폴더가 존재하는지 조회
        Folder folder = documentFolderRepository.findById(documentDto.getDfId())
                .orElseThrow(() -> new RequestException(ErrorCode.FOLDER_NOT_FOUND_404));

        // 3. 프로젝트 조회
        Project project = projectRepository.findById(folder.getProject().getId())
                // 3-1. 존재하지 않는 경우 예외처리
                .orElseThrow(() -> new RequestException(ErrorCode.PROJECT_NOT_FOUND_404));

        // 4. 유저 권한 조회
        Optional<ProjectMember> projectMember = projectMemberRepository.findByMemberIdAndProjectId(member.getId(), project.getId());
        if (projectMember.isPresent()) {
            // 5. 프로젝트에 참여 중인 멤버인 경우, 문서 생성 로직 실행
            if (projectMember.get().getDelFlag().equals(StatusFlag.NORMAL)) {
                // 6. 기존 문서 개수 카운트
                long cntDocuments = documentRepository.countAllByfolder_Id(folder.getId());
                // 7. 새 문서 객체를 생성한다.
                Document document = Document.builder()
                        .folder(folder)
                        .title(documentDto.getTitle())
                        .contents(documentDto.getContents())
                        .position((int) cntDocuments)
                        .nickname(member.getNickname())
                        .build();

                // 7. 새로 생성한 객체를 Repository 를 이용하여 DB에 저장한다
                return new DocumentDto(documentRepository.save(document));
            } else {
                // 5-1. 프로젝트에서 나갔거나 추방당한 멤버인 경우, 예외 처리
                throw new RequestException(ErrorCode.PROJECT_FORBIDDEN_403);
            }
        } else {
            throw new RequestException(ErrorCode.COMMON_BAD_REQUEST_400);
        }
    }

    /**
     * M1-6 문서수정
     */
    @Transactional
    public void modifyDocument(DocumentDto documentDto) {

        // 1. 파리미터로 받은 문서 객체에서 필요한 데이터를 추출한다
        UUID documentId = documentDto.getDocId();

        // 2. 문서 ID를 key로 해당 문서를 DB에서 조회한다.
        Optional<Document> optionalDocument = documentRepository.findById(documentId);
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

        Optional<Document> optionalDocument = documentRepository.findById(UUID.fromString(docId));
        Document document = optionalDocument.orElseThrow(
                () -> new RequestException(ErrorCode.DOCUMENT_NOT_FOUND_404));

        document.delete();

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

    }
}
