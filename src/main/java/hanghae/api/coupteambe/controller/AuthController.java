package hanghae.api.coupteambe.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import hanghae.api.coupteambe.domain.dto.JwtTokenDto;
import hanghae.api.coupteambe.domain.dto.ResResultDto;
import hanghae.api.coupteambe.domain.dto.social.SocialUserInfoDto;
import hanghae.api.coupteambe.service.AuthGoogleService;
import hanghae.api.coupteambe.service.AuthKakaoService;
import hanghae.api.coupteambe.service.AuthNaverService;
import hanghae.api.coupteambe.service.AuthService;
import hanghae.api.coupteambe.util.exception.ErrorCode;
import hanghae.api.coupteambe.util.exception.RequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthKakaoService authKakaoService;
    private final AuthGoogleService authGoogleService;
    private final AuthNaverService authNaverService;

    /**
     * <pre>
     *     ==소셜로그인 인증 절차==
     *     1. 프론트엔드에서 소셜로그인 시작 후 인증서버로부터 authorization code 발급받는다.
     *     2. authorization code 를 현재 메소드로 백엔드가 전달받는다.
     *     3. authorization code 를 이용하여 인증서버에게 사용자 인증 토큰을 요청/발급 받는다.
     *     4. 인증 토큰을 이용하여 소셜 API 서버에게 사용자 정보를 전달 받는다.
     *     5. 백엔드에서 사용자 정보를 토대로 로그인(회원가입)처리를 진행한다.
     *     6. 프론트엔드에게 자체 인증 토큰(JWT)를 전달하여 인증 완료를 알린다.
     * </pre>
     */
    @PostMapping("/{social}")
    public ResponseEntity<ResResultDto> login(
            @PathVariable("social") String socialPath, @RequestParam(name = "code") String code, String state,
            HttpServletResponse response) throws JsonProcessingException {

        /**
         * 2~4번 과정 수행
         * *authorization code를 이용하여, 사용자 정보(socialUserInfoDto)를 받아온다.
         */
        SocialUserInfoDto socialUserInfoDto = null;
        switch (socialPath) {
            case "kakao":
                socialUserInfoDto = authKakaoService.kakao(code);
                break;
            case "google":
                socialUserInfoDto = authGoogleService.google(code);
                break;
            case "naver":
                socialUserInfoDto = authNaverService.naver(code, state);
                break;
        }
        if (socialUserInfoDto == null) {
            throw new RequestException(ErrorCode.COMMON_BAD_REQUEST_400);
        }

        /**
         * 5번 수행
         * socialUserInfoDto 를 이용하여 자체 서비스의 사용자 인증을 처리한다.
         */
        JwtTokenDto jwtTokenDto = authService.login(socialUserInfoDto);

        setJwtCookie(response, jwtTokenDto);

        return ResponseEntity.ok(new ResResultDto("로그인 성공"));
    }

    @PostMapping("/reissue")
    public ResponseEntity<ResResultDto> reissue(HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        String refreshToken = null;

        if (cookies == null) {
            throw new RequestException(ErrorCode.JWT_NOT_FOUND_404);
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refreshToken")) {
                refreshToken = cookie.getValue();
            }
        }

        if (refreshToken == null) {
            throw new RequestException(ErrorCode.JWT_NOT_FOUND_404);
        }

        JwtTokenDto jwtTokenDto = authService.reissue(JwtTokenDto.builder()
                                                                 .refreshToken(refreshToken)
                                                                 .build());

        setJwtCookie(response, jwtTokenDto);

        return ResponseEntity.ok(new ResResultDto("JWT 갱신 완료"));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<ResResultDto> logout(HttpServletResponse response) {

        Cookie cookie = new Cookie("accessToken", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        cookie = new Cookie("refreshToken", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        authService.logout(loginId);

        return ResponseEntity.ok(new ResResultDto("로그아웃 성공"));
    }

    private void setJwtCookie(HttpServletResponse response, JwtTokenDto jwtTokenDto) {

        ResponseCookie responseCookie = ResponseCookie.from("accessToken", jwtTokenDto.getAccessToken())
                                                      .domain("localhost")
                                                      .httpOnly(true)
                                                      .maxAge(60 * 5)
                                                      .sameSite("None")
                                                      .secure(false)
                                                      .path("/").build();

        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());

        responseCookie = ResponseCookie.from("refreshToken", jwtTokenDto.getRefreshToken())
                                       .domain("localhost")
                                       .httpOnly(true)
                                       .maxAge(60 * 15)
                                       .sameSite("None")
                                       .secure(false)
                                       .path("/").build();

        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }
}
