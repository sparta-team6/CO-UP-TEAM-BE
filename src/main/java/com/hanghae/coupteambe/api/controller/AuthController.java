package com.hanghae.coupteambe.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanghae.coupteambe.api.domain.dto.JwtTokenDto;
import com.hanghae.coupteambe.api.domain.dto.ResResultDto;
import com.hanghae.coupteambe.api.domain.dto.social.SocialUserInfoDto;
import com.hanghae.coupteambe.api.service.sociallogin.AuthService;
import com.hanghae.coupteambe.api.service.sociallogin.SocialLoginService;
import com.hanghae.coupteambe.api.service.sociallogin.SocialLoginServiceMap;
import com.hanghae.coupteambe.api.util.exception.ErrorCode;
import com.hanghae.coupteambe.api.util.exception.RequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final SocialLoginServiceMap socialLoginServiceMap;


    @PostMapping("/{social}")
    public ResponseEntity<ResResultDto> login(
            @PathVariable("social") String socialPath, @RequestParam(name = "code") String code, String state,
            HttpServletResponse response) throws JsonProcessingException {

        SocialLoginService socialLoginService = socialLoginServiceMap.get(socialPath);
        if (socialLoginService == null) {
            throw new RequestException(ErrorCode.COMMON_BAD_REQUEST_400);
        }

        SocialUserInfoDto socialUserInfoDto = socialLoginService.socialLogin(code, state);
        if (socialUserInfoDto == null) {
            throw new RequestException(ErrorCode.COMMON_BAD_REQUEST_400);
        }

        String loginId = authService.login(socialUserInfoDto);
        JwtTokenDto jwtTokenDto = authService.getJwtTokenDto(loginId);

        authService.setJwtCookie(response, jwtTokenDto);

        return ResponseEntity.ok(new ResResultDto("????????? ??????"));
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

        authService.setJwtCookie(response, jwtTokenDto);

        return ResponseEntity.ok(new ResResultDto("JWT ?????? ??????"));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<ResResultDto> logout(HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return ResponseEntity.ok(new ResResultDto("???????????? ??????"));
        }

        for (Cookie cookie : cookies) {
            log.info("cookie name: '{}'", cookie.getName());
            log.info("cookie value: '{}'", cookie.getValue());
            log.info("cookie domain: '{}'", cookie.getDomain());
        }

        ResponseCookie responseCookie = ResponseCookie.from("accessToken", "")
                .domain("cooperate-up.com")
                .httpOnly(false)
                .maxAge(0)
                .sameSite("None")
                .secure(true)
                .path("/").build();

        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());

        responseCookie = ResponseCookie.from("refreshToken", "")
                .domain("cooperate-up.com")
                .httpOnly(false)
                .maxAge(0)
                .sameSite("None")
                .secure(true)
                .path("/").build();

        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());

        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        authService.logout(loginId);

        return ResponseEntity.ok(new ResResultDto("???????????? ??????"));
    }
}
