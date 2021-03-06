package com.hanghae.coupteambe.api.service.sociallogin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae.coupteambe.api.domain.dto.social.SocialUserInfoDto;
import com.hanghae.coupteambe.api.enumerate.Social;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class AuthGoogleService implements SocialLoginService {

    @Value("${auth.google.client-id}")
    private String googleClientId;

    @Value("${auth.google.client-secret}")
    private String googleClientSecret;

    @Value("${auth.google.redirect-uri}")
    private String googleRedirectUri;

    // 구글 로그인
    public SocialUserInfoDto socialLogin(String code, String state) throws JsonProcessingException {

        // 인가코드로 엑세스토큰 가져오기
        String accessToken = getAccessToken(code, state);

        // 엑세스토큰으로 유저정보 가져오기
        return getUserInfo(accessToken);

    }

    // 인가코드로 엑세스토큰 가져오기
    public String getAccessToken(String code, String state) throws JsonProcessingException {

        // 헤더에 Content-type 지정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 바디에 필요한 정보 담기
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", googleClientId);
        body.add("client_secret", googleClientSecret);
        body.add("code", code);
        body.add("redirect_uri", googleRedirectUri);
        body.add("grant_type", "authorization_code");

        // POST 요청 보내기
        HttpEntity<MultiValueMap<String, String>> googleToken = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://oauth2.googleapis.com/token",
                HttpMethod.POST, googleToken,
                String.class
        );

        // response에서 엑세스토큰 가져오기
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseToken = objectMapper.readTree(responseBody);
        String accessToken = responseToken.get("access_token").asText();
        log.debug("access token : " + accessToken);

        return accessToken;
    }

    // 엑세스토큰으로 유저정보 가져오기
    public SocialUserInfoDto getUserInfo(String accessToken) throws JsonProcessingException {

        // 헤더에 엑세스토큰 담기, Content-type 지정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer" + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // POST 요청 보내기
        HttpEntity<MultiValueMap<String, String>> googleUser = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://openidconnect.googleapis.com/v1/userinfo",
                HttpMethod.POST, googleUser,
                String.class
        );

        // response 에서 유저정보 가져오기
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode googleUserInfo = objectMapper.readTree(responseBody);

        // 유저정보 작성
        String id = googleUserInfo.get("sub").asText();
        String email = googleUserInfo.get("email").asText();
        String nickname = googleUserInfo.get("name").asText();
        String profile_image = googleUserInfo.get("picture").asText();

        log.debug("로그인 이용자 정보");
        log.debug("구글 고유 ID : " + id);
        log.debug("닉네임 : " + nickname);
        log.debug("이메일 : " + email);
        log.debug("프로필이미지 URL : " + profile_image);

        return SocialUserInfoDto.builder()
                .loginId(email)
                .nickname(nickname)
                .profileImage(profile_image)
                .social(Social.GOOGLE).build();
    }
}
