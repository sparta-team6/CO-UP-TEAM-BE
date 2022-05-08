package hanghae.api.coupteambe.service.member;

import hanghae.api.coupteambe.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

//    @Transactional
//    ResMemberInfoDto getMemberInfo(String loginId, Social social) {
//        memberRepository.findByLoginIdAndSocial(loginId, social);
//    }
}
