package hanghae.api.coupteambe.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hanghae.api.coupteambe.domain.dto.social.SocialUserInfoDto;
import hanghae.api.coupteambe.enumerate.Social;
import hanghae.api.coupteambe.util.exception.ErrorCode;
import hanghae.api.coupteambe.util.exception.RequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class AuthNaverService {

    public SocialUserInfoDto naver(String code, String state) throws JsonProcessingException {
    String accessToken = getAccessToken(code, state);
    return getnaverUserInfo(accessToken);
}

        @Value("${auth.naver.client-id}")
        private String naverClientKId;

        @Value("${auth.naver.client-secret}")
        private String naverClientSecret;

        @Value("${auth.naver.redirect-uri}")
        private String naverRedirectUri;

        private String getAccessToken(String code, String state) throws JsonProcessingException {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "authorization_code");
            body.add("client_id", naverClientKId);
            body.add("client_secret", naverClientSecret);
            body.add("redirect_uri", naverRedirectUri);
            body.add("code", code);
            body.add("state", state);

            HttpEntity<MultiValueMap<String, String>> naverTokenRequest = new HttpEntity<>(body, headers);
            RestTemplate rt = new RestTemplate();
            try {
                ResponseEntity<String> response = rt.exchange(
                        "https://nid.naver.com/oauth2.0/token",
                        HttpMethod.POST,
                        naverTokenRequest,
                        String.class
                );


                // HTTP 응답 (JSON) -> 액세스 토큰 파싱
                String responseBody = response.getBody();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                String access_token = jsonNode.get("access_token").asText();
                String refresh_token = jsonNode.get("refresh_token").asText();
                log.debug("access token : " + access_token);
                log.debug("refresh token : " + refresh_token);

                return access_token;
            } catch (HttpClientErrorException e) {
                throw new RequestException(ErrorCode.COMMON_BAD_REQUEST_400);
            }
        }

        private SocialUserInfoDto getnaverUserInfo(String accessToken) throws JsonProcessingException {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            HttpEntity<MultiValueMap<String, String>> naverUserInfoRequest = new HttpEntity<>(headers);
            RestTemplate rt = new RestTemplate();
            ResponseEntity<String> response = rt.exchange(
                    "https://openapi.naver.com/v1/nid/me",
                    HttpMethod.POST,
                    naverUserInfoRequest,
                    String.class
            );

            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            String id = jsonNode.get("response").get("id").asText();
            String nickname = jsonNode.get("response").get("nickname").asText();
            String email = jsonNode.get("response").get("email").asText();
            String profile_image = jsonNode.get("response").get("profile_image").asText();

            log.debug("로그인 이용자 정보");
            log.debug("네이버 고유ID : " + id);
            log.debug("닉네임 : " + nickname);
            log.debug("이메일 : " + email);
            log.debug("프로필이미지 URL : " + profile_image);

            return SocialUserInfoDto.builder()
                    .loginId(email)
                    .nickname(nickname)
                    .profileImage(profile_image)
                    .social(Social.NAVER).build();
        }
}
