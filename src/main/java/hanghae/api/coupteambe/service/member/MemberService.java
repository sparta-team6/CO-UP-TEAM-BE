package hanghae.api.coupteambe.service.member;

import hanghae.api.coupteambe.domain.dto.member.ReqMemberInfoDto;
import hanghae.api.coupteambe.domain.dto.member.ResMemberInfoDto;
import hanghae.api.coupteambe.domain.entity.member.Member;
import hanghae.api.coupteambe.domain.entity.project.Project;
import hanghae.api.coupteambe.domain.repository.member.MemberRepository;
import hanghae.api.coupteambe.domain.repository.member.MemberRepositoryImpl;
import hanghae.api.coupteambe.domain.repository.project.ProjectRepository;
import hanghae.api.coupteambe.util.exception.ErrorCode;
import hanghae.api.coupteambe.util.exception.RequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepositoryImpl memberRepositoryImpl;

    @Transactional
    ResMemberInfoDto getMemberInfo(String loginId) {

        Optional<Member> optionalMember = memberRepository.findByLoginId(loginId);

        Member member = optionalMember.orElseThrow(() -> new RequestException(ErrorCode.MEMBER_LOGINID_NOT_FOUND_404));
        return new ResMemberInfoDto(member);
    }

    /**
     * M4-5. 회원 정보 수정
     */
    @Transactional
    public void updateMemberInfo(ReqMemberInfoDto reqMemberInfoDto) {

        // 1. Request 에서 유저 로그인 ID(Email) 추출
        String loginId = reqMemberInfoDto.getLoginId();

        // 2. 로그인 ID 로 DB 에서 유저 조회
        Optional<Member> optionalMember = memberRepository.findByLoginId(loginId);
        // 2-1. DB 에서 조회되지 않는 경우, 404에러 반환
        Member member = optionalMember.orElseThrow(() -> new RequestException(ErrorCode.MEMBER_LOGINID_NOT_FOUND_404));

        // 3. 조회에 성공했을 시, 유저 정보 변경
        member.updateMember(reqMemberInfoDto);
    }

    /**
     * M4-7. 프로젝트 멤버 전체 조회
     */
    public List<ResMemberInfoDto> getProjectMembers(UUID pjId) {
        // 1. 파라매터로 받은 프로젝트 ID 를 Key 로 해당 프로젝트가 존재하는지 조회
        Optional<Project> optionalProject = projectRepository.findById(pjId);
        // 1-1. 프로젝트가 존재하지 않는 경우, 예외 처리
        optionalProject.orElseThrow(() -> new RequestException(ErrorCode.PROJECT_NOT_FOUND_404));

        // 2. 프로젝트가 존재하는 경우, 프로젝트 ID 를 Key 로 프로젝트에 참가한 전체 멤버 조회
        return memberRepositoryImpl.findMembersFromProjectByProjectId_DSL(pjId);
    }


}
