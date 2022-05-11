package hanghae.api.coupteambe.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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

    @Transactional
    public JwtTokenDto login(SocialUserInfoDto socialUserInfoDto) {

        Member member = new Member(socialUserInfoDto);

        //가입되지 않은 사용자이면 데이터베이스에 저장한다. (회원가입)
        if (!memberRepository.existsMemberByLoginId(member.getLoginId())) {
            member.updatePassword(passwordEncoder.encode(socialUserInfoDto.getLoginId()));
            memberRepository.save(member);
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(member.getLoginId(), member.getPassword());
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
    @Transactional
    public JwtTokenDto reissue(JwtTokenDto jwtTokenDto) {


        //fixme refresh 토큰이 만료일 경우 쿠키 삭제, 데이터베이스 삭제 등 처리가 필요하다.( 만료될 경우 예외처리되어 난감하다. )
        tokenProvider.validateToken(jwtTokenDto.getRefreshToken());

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

    public SocialUserInfoDto kakao(String code) throws JsonProcessingException {

        //todo 프론트에서 받은 인가코드를 기반으로 인증서버에게 인증 받고,
        // 인증받은 사용자의 정보를 이용하여 SocialUserInfoDto를 생성하여 반환한다.

        //프론트에서 받은 인가코드를 기반으로 인증서버에게 인증 받고,
        String accessToken = getAccessToken(code);

        //인증받은 사용자의 정보를 이용하여 SocialUserInfoDto 를 생성하여 반환한다.
        return getKakaoUserInfo(accessToken);
    }

    @Value("${auth.kakao.client-id}")
    private String kakaoClientKId;

    @Value("${auth.kakao.redirect-uri}")
    private String kakaoRedirectUri;
    // 1. "인가 코드"로 "액세스 토큰" 요청
    private String getAccessToken(String code) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoClientKId);
        body.add("redirect_uri", kakaoRedirectUri);
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
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
    }

    // 2. "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
    private SocialUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties").get("nickname").asText();
        String email = jsonNode.get("kakao_account").get("email").asText();
        String profile_image = jsonNode.get("properties").get("profile_image").asText();

        log.debug("로그인 이용자 정보");
        log.debug("카카오 고유ID : " + id);
        log.debug("닉네임 : " + nickname);
        log.debug("이메일 : " + email);
        log.debug("프로필이미지 URL : " + profile_image);

        return generateSocialUserInfoDto(email, nickname, profile_image, Social.KAKAO);
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
