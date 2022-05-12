package hanghae.api.coupteambe.domain;

import hanghae.api.coupteambe.domain.entity.member.Member;
import hanghae.api.coupteambe.domain.repository.member.MemberRepository;
import hanghae.api.coupteambe.util.SecurityUtil;
import hanghae.api.coupteambe.util.exception.ErrorCode;
import hanghae.api.coupteambe.util.exception.RequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuditorAware implements AuditorAware<UUID> {

    private final MemberRepository memberRepository;

    @Override
    public Optional<UUID> getCurrentAuditor() {

        log.debug("getCurrentAuditor()");

        Optional<String> optionalLoginid = SecurityUtil.getCurrentUsername();

        if (optionalLoginid.isPresent()) {
            Optional<Member> optionalMember = memberRepository.findByLoginId(optionalLoginid.get());
            Member member = optionalMember.orElseThrow(
                    () -> new RequestException(ErrorCode.MEMBER_LOGINID_NOT_FOUND_404));

            return Optional.of(member.getId());
        } else {
            return Optional.empty();
        }
    }
}
