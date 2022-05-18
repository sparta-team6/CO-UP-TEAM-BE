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
import hanghae.api.coupteambe.util.exception.ErrorCode;
import hanghae.api.coupteambe.util.exception.RequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        // 1-1. 일반 유저 접근 시, 예외 처리
//        if(!projectMember.getRole().equals(ProjectRole.ADMIN)) {
//            throw new RequestException(ErrorCode.NO_PERMISSION_TO_WRITE_NOTICE);
//        }

//        // 2. 공지사항을 업로드할 대상 프로젝트가 존재하는지 확인
//        UUID mbId = projectMember.getMember().getId();
//        UUID pjId = noticeInfoDto.getPjId();

        String loginId = getCurrentUsername()
                .orElseThrow(() -> new RequestException(ErrorCode.COMMON_BAD_REQUEST_400));

        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RequestException(ErrorCode.MEMBER_LOGINID_NOT_FOUND_404));
        UUID mbId = member.getId();
        UUID pjId = noticeInfoDto.getPjId();

        ProjectMember projectMember = projectMemberRepository.findProjectMemberFromProjectMemberByPjIdAndMbId_DSL(pjId, mbId);

        // 3. 공지사항 객체 생성
        Notice notice = Notice.builder()
                .title(noticeInfoDto.getTitle())
                .contents(noticeInfoDto.getContents())
                .projectMember(projectMember)
                .build();

        // 4. 공지사항 저장
        noticeRepository.save(notice);

    }

    /**
     * O1-2. 공지사항 전체 조회
     */
    public List<NoticeInfoDto> getAllNotices(String pjId) {
        // 1. 프로젝트가 존재하는지 조회
        Project project = projectRepository.findById(UUID.fromString(pjId))
                .orElseThrow(() -> new RequestException(ErrorCode.PROJECT_NOT_FOUND_404));

        ProjectMember projectMember = projectMemberRepository.findTop1ByProjectId(UUID.fromString(pjId))
                .orElseThrow(() -> new RequestException(ErrorCode.PROJECT_NOT_FOUND_404));

        // 2. 공지사항 전체 조회
        return noticeRepository.findNoticesFromProjectByProjectMbId_DSL(projectMember.getId());
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
        // 1. 유저 권한 조회
        ProjectMember projectMember = searchUserInfo();

        // 1-1. 일반 유저 접근 시, 예외 처리
        if(!projectMember.getRole().equals(ProjectRole.ADMIN)) {
            throw new RequestException(ErrorCode.NO_PERMISSION_TO_MODIFY_NOTICE);
        }

        // 2. 해당 공지사항이 존재하는지 조회
        UUID noticeId = noticeInfoDto.getNoticeId();
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new RequestException(ErrorCode.NOTICE_NOT_FOUND_404));

        // 3. 공지사항 수정
        notice.updateNotice(noticeInfoDto);
    }

    /**
     * O1-5. 공지사항 글 삭제
     */
    @Transactional
    public void deleteNotice(String noticeId) {
        // 1. 유저 권한 조회
        ProjectMember projectMember = searchUserInfo();

        // 1-1. 일반 유저 접근 시, 예외 처리
        if(!projectMember.getRole().equals(ProjectRole.ADMIN)) {
            throw new RequestException(ErrorCode.NO_PERMISSION_TO_DELETE_NOTICE);
        }
        
        // 2. 공지사항 글 삭제
        noticeRepository.deleteById(UUID.fromString(noticeId));
    }

    /**
     * 유저 정보 조회
     */
    private ProjectMember searchUserInfo() {
        // 1. 로그인한 유저 아이디 추출
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        // 2. 유저가 Member DB 에 존재하는지 조회
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RequestException(ErrorCode.MEMBER_LOGINID_NOT_FOUND_404));
        // 3. 유저가 ProjectMember DB 에 존재하는지 조회
        return projectMemberRepository.findByMember(member)
                .orElseThrow(() -> new RequestException(ErrorCode.MEMBER_LOGINID_NOT_FOUND_404));
    }
}
