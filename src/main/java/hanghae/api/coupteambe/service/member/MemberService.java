package hanghae.api.coupteambe.service.member;

import hanghae.api.coupteambe.domain.dto.member.ReqMemberInfoDto;
import hanghae.api.coupteambe.domain.dto.member.ResMemberInfoDto;
import hanghae.api.coupteambe.domain.entity.member.Member;
import hanghae.api.coupteambe.domain.repository.member.MemberRepository;
import hanghae.api.coupteambe.util.exception.ErrorCode;
import hanghae.api.coupteambe.util.exception.RequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

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


}
