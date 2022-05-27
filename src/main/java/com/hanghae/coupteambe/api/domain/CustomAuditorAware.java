package com.hanghae.coupteambe.api.domain;

import com.hanghae.coupteambe.api.domain.entity.member.Member;
import com.hanghae.coupteambe.api.domain.repository.member.MemberRepository;
import com.hanghae.coupteambe.api.util.SecurityUtil;
import com.hanghae.coupteambe.api.util.exception.ErrorCode;
import com.hanghae.coupteambe.api.util.exception.RequestException;
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
            Optional<Member> optionalMember = memberRepository.findRequires_NewByLoginId(optionalLoginid.get());
            Member member = optionalMember.orElseThrow(
                    () -> new RequestException(ErrorCode.MEMBER_LOGINID_NOT_FOUND_404));

            return Optional.of(member.getId());
        } else {
            return Optional.empty();
        }
    }
}
