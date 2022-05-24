package hanghae.api.coupteambe.service;

import hanghae.api.coupteambe.domain.dto.notice.NoticeInfoDto;
import hanghae.api.coupteambe.domain.entity.member.Member;
import hanghae.api.coupteambe.domain.entity.notice.Notice;
import hanghae.api.coupteambe.domain.entity.project.Project;
import hanghae.api.coupteambe.domain.entity.project.ProjectMember;
import hanghae.api.coupteambe.domain.repository.member.MemberRepository;
import hanghae.api.coupteambe.domain.repository.notice.NoticeRepository;
import hanghae.api.coupteambe.domain.repository.project.ProjectMemberRepository;
import hanghae.api.coupteambe.domain.repository.project.ProjectRepository;
import hanghae.api.coupteambe.enumerate.ProjectRole;
import hanghae.api.coupteambe.enumerate.StatusFlag;
import hanghae.api.coupteambe.util.exception.ErrorCode;
import hanghae.api.coupteambe.util.exception.RequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static hanghae.api.coupteambe.util.SecurityUtil.getCurrentUsername;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final ProjectMemberRepository projectMemberRepository;
    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;

    /**
     * O1-1. 공지사항 글 생성
     */
    @Transactional
    public void createNotice(NoticeInfoDto noticeInfoDto) {
        // 1. 로그인 한 유저의 로그인 ID 추출
        String loginId = getCurrentUsername()
                // 1-1. 로그인 안된 경우 예외처리
                .orElseThrow(() -> new RequestException(ErrorCode.COMMON_BAD_REQUEST_400));

        // 2. DB 에 해당 유저가 존재하는지 확인
        Member member = memberRepository.findByLoginId(loginId)
                // 2-1. 존재하지 않는 경우 예외처리
                .orElseThrow(() -> new RequestException(ErrorCode.MEMBER_LOGINID_NOT_FOUND_404));

        // 3. 프로젝트 조회
        Project project = projectRepository.findById(noticeInfoDto.getPjId())
                // 3-1. 존재하지 않는 경우 예외처리
                .orElseThrow(() -> new RequestException(ErrorCode.PROJECT_NOT_FOUND_404));

        // 4. 유저 권한 조회
        Optional<ProjectMember> projectMember = projectMemberRepository.findByMemberIdAndProjectId(member.getId(), project.getId());
        if (projectMember.isPresent()) {
            // 4-1. 관리자인 경우에만 공지 작성 가능
            if (projectMember.get().getRole().equals(ProjectRole.ADMIN)) {
                // 5. 공지사항 객체 생성
                Notice notice = Notice.builder()
                        .title(noticeInfoDto.getTitle())
                        .contents(noticeInfoDto.getContents())
                        .projectMember(projectMember.get())
                        .build();
                // 6. 공지사항 저장
                noticeRepository.save(notice);
            } else {
                // 4-2. 관리자가 아닌 경우, 예외처리
                throw new RequestException(ErrorCode.NO_PERMISSION_TO_WRITE_NOTICE_400);
            }
        } else {
            throw new RequestException(ErrorCode.COMMON_BAD_REQUEST_400);
        }
    }

    /**
     * O1-2. 공지사항 전체 조회
     */
    public List<NoticeInfoDto> getAllNotices(String pjId) {
        // 1. 프로젝트가 존재하는지 조회
        projectRepository.findById(UUID.fromString(pjId))
                .orElseThrow(() -> new RequestException(ErrorCode.PROJECT_NOT_FOUND_404));

        // 2. 공지사항 전체 조회
        return noticeRepository.findAllNoticeByPjId_DSL(UUID.fromString(pjId));
    }

    /**
     * O1-3. 선택 공지사항 조회
     */
    public NoticeInfoDto getOneNotice(String noticeId) {
        // 1. 해당 공지사항이 존재하는지 조회
        Notice notice = noticeRepository.findById(UUID.fromString(noticeId))
                .orElseThrow(() -> new RequestException(ErrorCode.NOTICE_NOT_FOUND_404));
        return new NoticeInfoDto(notice);
    }

    /**
     * O1-4. 공지사항 글 수정
     */
    @Transactional
    public void modifyNotice(NoticeInfoDto noticeInfoDto) {
        // 1. 로그인 한 유저의 로그인 ID 추출
        String loginId = getCurrentUsername()
                // 1-1. 로그인 안된 경우 예외처리
                .orElseThrow(() -> new RequestException(ErrorCode.COMMON_BAD_REQUEST_400));

        // 2. DB 에 해당 유저가 존재하는지 확인
        Member member = memberRepository.findByLoginId(loginId)
                // 2-1. 존재하지 않는 경우 예외처리
                .orElseThrow(() -> new RequestException(ErrorCode.MEMBER_LOGINID_NOT_FOUND_404));

        // 3. 프로젝트 조회
        Project project = projectRepository.findById(noticeInfoDto.getPjId())
                // 3-1. 존재하지 않는 경우 예외처리
                .orElseThrow(() -> new RequestException(ErrorCode.PROJECT_NOT_FOUND_404));

        // 4. 유저 권한 조회
        Optional<ProjectMember> projectMember = projectMemberRepository.findByMemberIdAndProjectId(member.getId(), project.getId());
        if (projectMember.isPresent()) {
            // 4-1. 관리자인 경우에만 공지 작성 가능
            if (projectMember.get().getRole().equals(ProjectRole.ADMIN)) {
                // 2. 해당 공지사항이 존재하는지 조회
                UUID noticeId = noticeInfoDto.getNoticeId();
                Notice notice = noticeRepository.findById(noticeId)
                        .orElseThrow(() -> new RequestException(ErrorCode.NOTICE_NOT_FOUND_404));

                // 3. 공지사항 수정
                notice.updateNotice(noticeInfoDto);
            } else {
                // 4-2. 관리자가 아닌 경우, 예외처리
                throw new RequestException(ErrorCode.NO_PERMISSION_TO_WRITE_NOTICE_400);
            }
        } else {
            throw new RequestException(ErrorCode.COMMON_BAD_REQUEST_400);
        }
    }

    /**
     * O1-5. 공지사항 글 삭제
     */
    @Transactional
    public void deleteNotice(NoticeInfoDto noticeInfoDto) {

        // 1. 로그인 한 유저의 로그인 ID 추출
        String loginId = getCurrentUsername()
                // 1-1. 로그인 안된 경우 예외처리
                .orElseThrow(() -> new RequestException(ErrorCode.COMMON_BAD_REQUEST_400));

        // 2. DB 에 해당 유저가 존재하는지 확인
        Member member = memberRepository.findByLoginId(loginId)
                // 2-1. 존재하지 않는 경우 예외처리
                .orElseThrow(() -> new RequestException(ErrorCode.MEMBER_LOGINID_NOT_FOUND_404));

        // 3. 프로젝트 조회
        Project project = projectRepository.findById(noticeInfoDto.getPjId())
                // 3-1. 존재하지 않는 경우 예외처리
                .orElseThrow(() -> new RequestException(ErrorCode.PROJECT_NOT_FOUND_404));

        // 4. 유저 권한 조회
        Optional<ProjectMember> projectMember = projectMemberRepository.findByMemberIdAndProjectId(member.getId(), project.getId());
        if (projectMember.isPresent()) {
            // 4-1. 관리자인 경우에만 공지 삭제 가능
            if (projectMember.get().getRole().equals(ProjectRole.ADMIN)) {

                Notice notice = noticeRepository.findById(noticeInfoDto.getNoticeId())
                        .orElseThrow(() -> new RequestException(ErrorCode.NOTICE_NOT_FOUND_404));

                notice.updateDelFlag(StatusFlag.DELETED);

            } else {
                // 4-2. 관리자가 아닌 경우, 예외처리
                throw new RequestException(ErrorCode.NO_PERMISSION_TO_DELETE_NOTICE_400);
            }
        } else {
            throw new RequestException(ErrorCode.COMMON_BAD_REQUEST_400);
        }
    }
}
