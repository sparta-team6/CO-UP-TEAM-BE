package hanghae.api.coupteambe.service.member;

import hanghae.api.coupteambe.domain.entity.member.Member;
import hanghae.api.coupteambe.domain.repository.member.MemberRepository;
import hanghae.api.coupteambe.util.exception.ErrorCode;
import hanghae.api.coupteambe.util.exception.RequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return memberRepository.findByLoginId(username)
                               .map(this::createUserDetails)
                               .orElseThrow(() -> new RequestException(ErrorCode.MEMBER_LOGINID_NOT_FOUND_404));
    }

    private UserDetails createUserDetails(Member member) {

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getRole().toString());

        return new User(
                String.valueOf(member.getLoginId()),
                member.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }
}
