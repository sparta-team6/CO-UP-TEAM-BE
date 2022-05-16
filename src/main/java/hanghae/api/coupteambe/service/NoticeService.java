package hanghae.api.coupteambe.service;

import hanghae.api.coupteambe.domain.dto.notice.NoticeInfoDto;
import hanghae.api.coupteambe.domain.entity.member.Member;
import hanghae.api.coupteambe.domain.entity.notice.Notice;
import hanghae.api.coupteambe.domain.entity.project.ProjectMember;
import hanghae.api.coupteambe.domain.repository.member.MemberRepository;
import hanghae.api.coupteambe.domain.repository.notice.NoticeRepository;
import hanghae.api.coupteambe.domain.repository.notice.NoticeRepositoryImpl;
import hanghae.api.coupteambe.domain.repository.project.ProjectMemberRepository;
import hanghae.api.coupteambe.enumerate.Role;
import hanghae.api.coupteambe.util.exception.ErrorCode;
import hanghae.api.coupteambe.util.exception.RequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final ProjectMemberRepository projectMemberRepository;
    private final NoticeRepository noticeRepository;
    private final NoticeRepositoryImpl noticeRepositoryImpl;
    private final MemberRepository memberRepository;

    /**
     * O1-1. 공지사항 글 생성
     */
    @Transactional
    public void createNotice(NoticeInfoDto noticeInfoDto) {
        // 1. 유저 권한 조회
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Member> optionalMember = memberRepository.findByLoginId(loginId);
        Member member = optionalMember
                .orElseThrow(() -> new RequestException(ErrorCode.MEMBER_LOGINID_NOT_FOUND_404));

        // 1-1. 일반 유저 접근 시, 예외 처리
        if(member.getRole().equals(Role.ROLE_USER)) {
            throw new RequestException(ErrorCode.PERMISSION_DENIED_403);
        }

        // 2. 공지사항을 업로드할 대상 프로젝트가 존재하는지 확인
        UUID pJMbId = noticeInfoDto.getPjMbId();
        Optional<ProjectMember> optionalProjectMember = projectMemberRepository.findById(pJMbId);
        ProjectMember projectMember = optionalProjectMember.orElseThrow(() -> new RequestException(ErrorCode.PROJECT_NOT_FOUND_404));

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
    public List<NoticeInfoDto> getAllNotices(String pjMbId) {
        // 1. 프로젝트가 존재하는지 조회
        Optional<ProjectMember> optionalProjectMember = projectMemberRepository.findById(UUID.fromString(pjMbId));
        ProjectMember projectMember = optionalProjectMember.orElseThrow(() -> new RequestException(ErrorCode.PROJECT_NOT_FOUND_404));

        // 2. 공지사항 전체 조회
        return noticeRepositoryImpl.findNoticesFromProjectByProjectMbId_DSL(UUID.fromString(pjMbId));
    }

    /**
     * O1-3. 선택 공지사항 조회
     */
    public NoticeInfoDto getOneNotice(String noticeId) {
        // 1. 해당 공지사항이 존재하는지 조회
        Optional<Notice> optionalNotice = noticeRepository.findById(UUID.fromString(noticeId));
        Notice notice = optionalNotice.orElseThrow(() -> new RequestException(ErrorCode.NOTICE_NOT_FOUND_404));
        return new NoticeInfoDto(notice);
    }

    /**
     * O1-4. 공지사항 글 수정
     */
    @Transactional
    public void modifyNotice(NoticeInfoDto noticeInfoDto) {
        // 1. 유저 권한 조회
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Member> optionalMember = memberRepository.findByLoginId(loginId);
        Member member = optionalMember
                .orElseThrow(() -> new RequestException(ErrorCode.MEMBER_LOGINID_NOT_FOUND_404));

        // 1-1. 일반 유저 접근 시, 예외 처리
        if(member.getRole().equals(Role.ROLE_USER)) {
            throw new RequestException(ErrorCode.PERMISSION_DENIED_403);
        }

        // 2. 해당 공지사항이 존재하는지 조회
        UUID noticeId = noticeInfoDto.getNoticeId();
        Optional<Notice> optionalNotice = noticeRepository.findById(noticeId);
        Notice notice = optionalNotice.orElseThrow(() -> new RequestException(ErrorCode.NOTICE_NOT_FOUND_404));

        // 3. 공지사항 수정
        notice.updateNotice(noticeInfoDto);
    }

    /**
     * O1-5. 공지사항 글 삭제
     */
    @Transactional
    public void deleteNotice(String noticeId) {
        // 1. 유저 권한 조회
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Member> optionalMember = memberRepository.findByLoginId(loginId);
        Member member = optionalMember
                .orElseThrow(() -> new RequestException(ErrorCode.MEMBER_LOGINID_NOT_FOUND_404));

        // 1-1. 일반 유저 접근 시, 예외 처리
        if(member.getRole().equals(Role.ROLE_USER)) {
            throw new RequestException(ErrorCode.PERMISSION_DENIED_403);
        }
        
        // 2. 공지사항 글 삭제
        noticeRepository.deleteById(UUID.fromString(noticeId));
    }
}
