package hanghae.api.coupteambe.security.jwt;

import hanghae.api.coupteambe.util.exception.ErrorCode;
import hanghae.api.coupteambe.util.exception.RequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
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

        String accessToken = getAccessToken(request);
        String requestURI = request.getRequestURI();

        if (StringUtils.hasText(accessToken) && tokenProvider.validateToken(accessToken)) {
            Authentication authentication = tokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Security Context에 Member: '{}' 인증 정보를 저장했습니다. URI: '{}'", authentication.getName(), requestURI);
        } else {
            log.debug("유효한 JWT 토큰이 없습니다. URI: '{}'", requestURI);
            throw new RequestException(ErrorCode.JWT_NOT_FOUND_404);
        }

        filterChain.doFilter(request, response);
    }

    private String getAccessToken(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        String accessToken = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("accessToken")) {
                accessToken = cookie.getValue();
                break;
            }
        }

        if (!StringUtils.hasText(accessToken)) {
            return null;
        }

        return accessToken;
    }
}
