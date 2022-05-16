package hanghae.api.coupteambe.service.member;

import hanghae.api.coupteambe.domain.dto.member.ReqMemberInfoDto;
import hanghae.api.coupteambe.domain.dto.member.ResMemberInfoDto;
import hanghae.api.coupteambe.domain.entity.member.Member;
import hanghae.api.coupteambe.domain.repository.member.MemberRepository;
import hanghae.api.coupteambe.domain.repository.member.MemberRepositoryImpl;
import hanghae.api.coupteambe.domain.repository.project.ProjectRepository;
import hanghae.api.coupteambe.util.exception.ErrorCode;
import hanghae.api.coupteambe.util.exception.RequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    @Transactional
    void updateMemberInfo(ReqMemberInfoDto reqMemberInfoDto) {

        String loginId = reqMemberInfoDto.getLoginId();

        Optional<Member> optionalMember = memberRepository.findByLoginId(loginId);
        Member member = optionalMember.orElseThrow(() -> new RequestException(ErrorCode.MEMBER_LOGINID_NOT_FOUND_404));

        member.updateMember(reqMemberInfoDto);
    }


    /**
     * M4-8 멤버 검색(1인)
     */
    @Transactional
    public ResMemberInfoDto getOneMemberInfo(String loginId) {

        // 1. 파라미터로 받은 loginId를 key로 멤버 정보를 조회한다
        Optional<Member> optionalMember = memberRepository.findByLoginId(loginId);
        Member member = optionalMember.orElseThrow(
                () -> new RequestException(ErrorCode.MEMBER_LOGINID_NOT_FOUND_404));

        // 2. 새 멤버 객체를 생성하고 조회한 멤버로 초기화한다.
        // 3. 생성된 멤버 객체를 반환한다.

        return new ResMemberInfoDto(member);
    }
    /// TODO: 2022/05/16 테스트해보자

    /**
     * M4-9 본인 정보 조회
     */
    public ResMemberInfoDto getMyInfo() {
        // 1. 현재 로그인한 유저의 ID 조회
        String LoginId = SecurityContextHolder.getContext().getAuthentication().getName();
        // 2. 현재 멤버가 존재하지 않는 경우 예외처리
        Optional<Member> optionalMember = memberRepository.findByLoginId(LoginId);
        Member member = optionalMember.orElseThrow(() -> new RequestException(ErrorCode.MEMBER_LOGINID_NOT_FOUND_404));

        // 3. 해당 멤버 조회 후 반환
        return new ResMemberInfoDto(member);
    }


}
