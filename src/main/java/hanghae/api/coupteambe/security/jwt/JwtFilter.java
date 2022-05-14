package hanghae.api.coupteambe.security.jwt;

import hanghae.api.coupteambe.domain.dto.JwtTokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        log.debug("doFilterInternal()");

        //todo 프론트엔드 테스트용, 테스트 후 지워야함
        String accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJnbG9iYWxzaDFAZ21haWwuY29tIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MjI1MjUyNzc3M30.t2ZqCdU5XLOX-mwue7dKupT6DVQCsvb7wBz7PqXOCIzQMPXgKkSvZYKEzjAtVvedeSMA9pkuJK06GNoblaQwrw";
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //todo 프론트엔드 테스트용, 테스트 후 지워야함

//        JwtTokenDto jwtTokenDto = getAccessToken(request);
//        String accessToken = jwtTokenDto == null ? null : jwtTokenDto.getAccessToken();
//        String refreshToken = jwtTokenDto == null ? null : jwtTokenDto.getRefreshToken();
//        String requestURI = request.getRequestURI();
//
//        if (StringUtils.hasText(accessToken) && tokenProvider.validateToken(accessToken)) {
//            Authentication authentication = tokenProvider.getAuthentication(accessToken);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            log.debug("AccessToken으로 Security Context에 Member: '{}' 인증 정보를 저장했습니다. URI: '{}'", authentication.getName(),
//                    requestURI);
//        } else if (StringUtils.hasText(accessToken)) {
//            log.debug("access 토큰이 만료되었습니다.");
//            throw new RequestException(ErrorCode.JWT_UNAUTHORIZED_401);
//        } else if (StringUtils.hasText(refreshToken) && tokenProvider.validateToken(refreshToken)) {
//            Authentication authentication = tokenProvider.getAuthentication(refreshToken);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            log.debug("RefreshToken으로 Security Context에 Member: '{}' 인증 정보를 저장했습니다. URI: '{}'",
//                    authentication.getName(), requestURI);
//        } else if (StringUtils.hasText(refreshToken)) {
//            log.debug("refresh 토큰이 만료되었습니다.");
//            throw new RequestException(ErrorCode.JWT_UNAUTHORIZED_401);
//        }

        filterChain.doFilter(request, response);
    }

    private JwtTokenDto getAccessToken(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return null;
        }

        String accessToken = null;
        String refreshToken = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("accessToken")) {
                accessToken = cookie.getValue();
            }
            if (cookie.getName().equals("refreshToken")) {
                refreshToken = cookie.getValue();
            }
        }

        return JwtTokenDto.builder()
                          .accessToken(accessToken)
                          .refreshToken(refreshToken).build();
    }
}
