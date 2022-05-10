package hanghae.api.coupteambe.domain;

import hanghae.api.coupteambe.domain.entity.member.Member;
import hanghae.api.coupteambe.domain.repository.MemberRepository;
import hanghae.api.coupteambe.util.exception.ErrorCode;
import hanghae.api.coupteambe.util.exception.RequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomAuditorAware implements AuditorAware<UUID> {

    private final MemberRepository memberRepository;

    @Override
    public Optional<UUID> getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        Optional<Member> member = memberRepository.findByLoginId(authentication.getName());
        if (member.isPresent()) {
            return Optional.of(member.get().getId());
        } else {
            throw new RequestException(ErrorCode.MEMBER_LOGINID_NOT_FOUND_404);
        }
    }
}