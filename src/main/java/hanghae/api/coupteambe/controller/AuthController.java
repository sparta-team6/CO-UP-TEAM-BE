package hanghae.api.coupteambe.controller;

import hanghae.api.coupteambe.domain.dto.JwtTokenDto;
import hanghae.api.coupteambe.domain.dto.ResResultDto;
import hanghae.api.coupteambe.domain.dto.social.SocialUserInfoDto;
import hanghae.api.coupteambe.enumerate.Social;
import hanghae.api.coupteambe.service.AuthService;
import hanghae.api.coupteambe.util.exception.ErrorCode;
import hanghae.api.coupteambe.util.exception.RequestException;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/{social}")
    public ResponseEntity<ResResultDto> login(
            @PathVariable("social") String socialPath, @RequestParam(name = "code") String code,HttpServletResponse response) {
        SocialUserInfoDto socialUserInfoDto = null;
        switch (socialPath) {
            case "kakao":
                socialUserInfoDto = authService.kakao(code);

                break;
            case "google":
                socialUserInfoDto = authService.google(code);
                break;
            case "github":
                socialUserInfoDto = authService.github(code);
                break;
        }
        if (socialUserInfoDto == null) {
            throw new RequestException(ErrorCode.COMMON_BAD_REQUEST_400);
        }


        JwtTokenDto jwtTokenDto = authService.login(socialUserInfoDto);

        setJwtCookie(response, jwtTokenDto);

        return ResponseEntity.ok(new ResResultDto("로그인 성공"));
    }

    @PostMapping("/reissue")
    public ResponseEntity<ResResultDto> reissue(HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        String accessToken = null;
        String refreshToken = null;

        if (cookies == null) {
            throw new RequestException(ErrorCode.JWT_NOT_FOUND_404);
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("accessToken")) {
                accessToken = cookie.getValue();
            }
            if (cookie.getName().equals("refreshToken")) {
                refreshToken = cookie.getValue();
            }
        }

        if (accessToken == null || refreshToken == null) {
            throw new RequestException(ErrorCode.JWT_NOT_FOUND_404);
        }

        JwtTokenDto jwtTokenDto = authService.reissue(JwtTokenDto.builder()
                                                             .accessToken(accessToken)
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
        Cookie cookie = new Cookie("accessToken", jwtTokenDto.getAccessToken());
        cookie.setMaxAge(60);
        response.addCookie(cookie);

        cookie = new Cookie("refreshToken", jwtTokenDto.getRefreshToken());
        cookie.setMaxAge(60 * 5);
        response.addCookie(cookie);
    }
}
