package com.hanghae.coupteambe.api.service.sociallogin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanghae.coupteambe.api.domain.dto.JwtTokenDto;
import com.hanghae.coupteambe.api.domain.dto.social.SocialUserInfoDto;
import com.hanghae.coupteambe.api.domain.entity.member.Member;
import com.hanghae.coupteambe.api.domain.entity.security.RefreshToken;
import com.hanghae.coupteambe.api.domain.repository.RefreshTokenRepository;
import com.hanghae.coupteambe.api.domain.repository.member.MemberRepository;
import com.hanghae.coupteambe.api.enumerate.Social;
import com.hanghae.coupteambe.api.security.jwt.TokenProvider;
import com.hanghae.coupteambe.api.util.exception.ErrorCode;
import com.hanghae.coupteambe.api.util.exception.RequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
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
    private final AuthKakaoService authKakaoService;
    private final AuthGoogleService authGoogleService;
    private final AuthNaverService authNaverService;

    public void socialLogin(String socialPath, String code, String state, HttpServletResponse response) throws JsonProcessingException {
        SocialUserInfoDto socialUserInfoDto = null;
        switch (socialPath) {
            case "kakao":
                socialUserInfoDto = authKakaoService.socialLogin(code, "");
                break;
            case "google":
                socialUserInfoDto = authGoogleService.socialLogin(code, "");
                break;
            case "naver":
                socialUserInfoDto = authNaverService.socialLogin(code, state);
                break;
        }
        if (socialUserInfoDto == null) {
            throw new RequestException(ErrorCode.COMMON_BAD_REQUEST_400);
        }

        String loginId = login(socialUserInfoDto);
        JwtTokenDto jwtTokenDto = getJwtTokenDto(loginId);

        setJwtCookie(response, jwtTokenDto);
    }

    @Transactional
    public String login(SocialUserInfoDto socialUserInfoDto) {

        Member member = new Member(socialUserInfoDto);

        //가입되지 않은 사용자이면 데이터베이스에 저장한다. (회원가입)
        if (!memberRepository.existsMemberByLoginId(member.getLoginId())) {
            member.updatePassword(passwordEncoder.encode(member.getLoginId()));
            memberRepository.save(member);
        }

        return member.getLoginId();
    }

    @Transactional
    public JwtTokenDto getJwtTokenDto(String loginId) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginId, loginId);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        JwtTokenDto jwtTokenDto = tokenProvider.generateTokenDto(authentication);

        // refresh 토큰이 데이터베이스에 있다면 갱신하고, 없다면 새로 저장.
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByLoginId(loginId);
        if (refreshToken.isPresent()) {
            refreshToken.get().updateToken(jwtTokenDto.getRefreshToken());
        } else {
            refreshTokenRepository.save(RefreshToken.builder()
                    .loginId(loginId)
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
    @Transactional
    public JwtTokenDto reissue(JwtTokenDto jwtTokenDto) {

        //fixme refresh 토큰이 만료일 경우 쿠키 삭제, 데이터베이스 삭제 등 처리가 필요하다.
        String refreshToken = jwtTokenDto.getRefreshToken();
        if (!tokenProvider.validateToken(refreshToken)) {
            String loginId = tokenProvider.getLoginId(refreshToken);
            refreshTokenRepository.deleteByLoginId(loginId);

            throw new RequestException(ErrorCode.JWT_UNAUTHORIZED_401);
        }

        // 사용자의 데이터베이스에 저장된 토큰 조회
        Authentication authentication = tokenProvider.getAuthentication(refreshToken);
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByLoginId(authentication.getName());

        // 데이터베이스에 토큰이 없다면, 로그아웃된 사용자
        if (!optionalRefreshToken.isPresent()) {
            throw new RequestException(ErrorCode.JWT_NOT_FOUND_404);
        }

        // 토큰정보 불일치.
        if (!optionalRefreshToken.get().getRefreshToken().equals(refreshToken)) {
            throw new RequestException(ErrorCode.JWT_NOT_ALLOWED_405);
        }

        JwtTokenDto newJwtTokenDto = tokenProvider.generateTokenDto(authentication);

        optionalRefreshToken.get().updateToken(newJwtTokenDto.getRefreshToken());

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

    public SocialUserInfoDto generateSocialUserInfoDto(String loginId, String nickname, String profileImage,
                                                        Social social) {
        return SocialUserInfoDto.builder()
                .loginId(loginId)
                .nickname(nickname)
                .profileImage(profileImage)
                .social(social).build();
    }

    public void setJwtCookie(HttpServletResponse response, JwtTokenDto jwtTokenDto) {

        ResponseCookie responseCookie = ResponseCookie.from("accessToken", jwtTokenDto.getAccessToken())
                .domain("cooperate-up.com")
                .httpOnly(false)
                .maxAge(60 * 30)
                .sameSite("None")
                .secure(true)
                .path("/").build();

        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());

        responseCookie = ResponseCookie.from("refreshToken", jwtTokenDto.getRefreshToken())
                .domain("cooperate-up.com")
                .httpOnly(false)
                .maxAge(60 * 60 * 24)
                .sameSite("None")
                .secure(true)
                .path("/").build();

        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }
}
