package hanghae.api.coupteambe.service;

import hanghae.api.coupteambe.domain.dto.JwtTokenDto;
import hanghae.api.coupteambe.domain.dto.social.SocialUserInfoDto;
import hanghae.api.coupteambe.domain.entity.member.Member;
import hanghae.api.coupteambe.domain.entity.security.RefreshToken;
import hanghae.api.coupteambe.domain.repository.MemberRepository;
import hanghae.api.coupteambe.domain.repository.RefreshTokenRepository;
import hanghae.api.coupteambe.enumerate.Social;
import hanghae.api.coupteambe.security.jwt.TokenProvider;
import hanghae.api.coupteambe.util.exception.ErrorCode;
import hanghae.api.coupteambe.util.exception.RequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtTokenDto login(SocialUserInfoDto socialUserInfoDto) {

        Member member = new Member(socialUserInfoDto);

        //가입되지 않은 사용자이면 데이터베이스에 저장한다. (회원가입)
        if (!memberRepository.existsMemberByLoginId(member.getLoginId())) {
            member.updatePassword(passwordEncoder.encode(member.getLoginId()));
            memberRepository.save(member);
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(member.getLoginId(), member.getLoginId());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        //XXX stateless 임을 생각하면 로그인 직후 contextholder 에서 보관할 이유가 없을것 같다.
        //SecurityContextHolder.getContext().setAuthentication(authentication);

        JwtTokenDto jwtTokenDto = tokenProvider.generateTokenDto(authentication);

        // refresh 토큰이 데이터베이스에 있다면 갱신하고, 없다면 새로 저장.
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByLoginId(member.getLoginId());
        if (refreshToken.isPresent()) {
            refreshToken.get().updateToken(jwtTokenDto.getRefreshToken());
        } else {
            refreshTokenRepository.save(RefreshToken.builder()
                                                    .loginId(member.getLoginId())
                                                    .refreshToken(jwtTokenDto.getRefreshToken())
                                                    .build());
        }

        return jwtTokenDto;
    }

    /**
     * <pre>
     *     사용자가 토큰 만료 예외를 만나면,
     *     재발급 요청을 하여 해당 메소드로 재발급을 진행한다.
     * </pre>
     */
    public JwtTokenDto reissue(JwtTokenDto jwtTokenDto) {

        //fixme refresh 토큰이 만료일 경우 쿠키 삭제, 데이터베이스 삭제 등 처리가 필요하다.
        if (!tokenProvider.validateToken(jwtTokenDto.getRefreshToken())) {
            String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
            refreshTokenRepository.deleteByLoginId(loginId);

            throw new RequestException(ErrorCode.JWT_UNAUTHORIZED_401);
        }

        // 사용자의 데이터베이스에 저장된 토큰 조회
        Authentication authentication = tokenProvider.getAuthentication(jwtTokenDto.getRefreshToken());
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByLoginId(authentication.getName());

        // 데이터베이스에 토큰이 없다면, 로그아웃된 사용자
        if (!refreshToken.isPresent()) {
            throw new RequestException(ErrorCode.JWT_NOT_FOUND_404);
        }

        // 토큰정보 불일치.
        if (!refreshToken.get().getRefreshToken().equals(jwtTokenDto.getRefreshToken())) {
            throw new RequestException(ErrorCode.JWT_NOT_ALLOWED_405);
        }

        JwtTokenDto newJwtTokenDto = tokenProvider.generateTokenDto(authentication);

        refreshToken.get().updateToken(newJwtTokenDto.getRefreshToken());

        return newJwtTokenDto;
    }

    @Transactional
    public void logout(String loginId) {
        refreshTokenRepository.deleteByLoginId(loginId);
    }

    public SocialUserInfoDto google(String code) {

        //todo 프론트에서 받은 인가코드를 기반으로 인증서버에게 인증 받고,
        // 인증받은 사용자의 정보를 이용하여 SocialUserInfoDto를 생성하여 반환한다.



        Social social = Social.GOOGLE;
        return generateSocialUserInfoDto(null, null, null, social);
    }

    public SocialUserInfoDto github(String code) {

        //todo 프론트에서 받은 인가코드를 기반으로 인증서버에게 인증 받고,
        // 인증받은 사용자의 정보를 이용하여 SocialUserInfoDto를 생성하여 반환한다.



        Social social = Social.GITHUB;
        return generateSocialUserInfoDto(null, null, null, social);
    }


    private SocialUserInfoDto generateSocialUserInfoDto(String loginId, String nickname, String profileImage,
            Social social) {
        return SocialUserInfoDto.builder()
                                .loginId(loginId)
                                .nickname(nickname)
                                .profileImage(profileImage)
                                .social(social).build();
    }
}
