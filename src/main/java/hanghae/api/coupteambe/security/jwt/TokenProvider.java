package hanghae.api.coupteambe.security.jwt;

import hanghae.api.coupteambe.domain.dto.JwtTokenDto;
import hanghae.api.coupteambe.util.exception.ErrorCode;
import hanghae.api.coupteambe.util.exception.RequestException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TokenProvider implements InitializingBean {

    private static final String AUTHORITIES_KEY = "auth";

    private final String secret;
    private final long accessTokenValidityInMilliseconds;
    private final long refreshTokenValidityInMilliseconds;

    private Key key;

    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-validity-in-milliseconds}") long accessTokenValidityInMilliseconds,
            @Value("${jwt.refresh-token-validity-in-milliseconds}") long refreshTokenValidityInMilliseconds) {

        this.secret = secret;
        this.accessTokenValidityInMilliseconds = accessTokenValidityInMilliseconds;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInMilliseconds;
    }


    @Override
    public void afterPropertiesSet() throws Exception {

        this.key = Keys.hmacShaKeyFor(this.secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * <pre>
     *     access 토큰과 refresh 토큰을 함께 발급
     * </pre>
     */
    public JwtTokenDto generateTokenDto(Authentication authentication) {

        String authorities = authentication.getAuthorities().stream()
                                           .map(GrantedAuthority::getAuthority)
                                           .collect(Collectors.joining(","));

        long now = (new Date().getTime());
        Date accessValidity = new Date(now + this.accessTokenValidityInMilliseconds);
        Date refreshValidity = new Date(now + this.refreshTokenValidityInMilliseconds);

        String accessToken = Jwts.builder()
                                 .setSubject(authentication.getName())
                                 .claim(AUTHORITIES_KEY, authorities)
                                 .signWith(key, SignatureAlgorithm.HS512)
                                 .setExpiration(accessValidity)
                                 .compact();

        String refreshToken = Jwts.builder()
                                  .setExpiration(refreshValidity)
                                  .signWith(key, SignatureAlgorithm.HS512)
                                  .compact();

        return JwtTokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiresIn(accessValidity.getTime())
                .build();
    }

    public Authentication getAuthentication(String token) {

        Claims claims = Jwts.parserBuilder()
                            .setSigningKey(key)
                            .build()
                            .parseClaimsJws(token)
                            .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                      .map(SimpleGrantedAuthority::new)
                      .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    /**
     * <pre>
     *     리턴값이 true이면 검증된 토큰.
     *            false이면 만료된 토큰이므로, refresh토큰 확인 후 재발급 or 인증 실패 처리해야함.
     *
     *     그 외, 토큰에 문제가 있는 경우 JwtException 발생(원인은 메세지에 기록함)
     * </pre>
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException exception) {
            log.debug("잘못된 JWT 서명입니다.");
            throw new RequestException(ErrorCode.JWT_BAD_TOKEN_400);
        } catch (UnsupportedJwtException e) {
            log.debug("지원되지 않는 JWT 토큰입니다.");
            throw new RequestException(ErrorCode.JWT_NOT_ALLOWED_405);
        } catch (IllegalArgumentException e) {
            log.debug("JWT 토큰이 잘못되었습니다.");
            throw new RequestException(ErrorCode.JWT_BAD_TOKEN_400);
        } catch (ExpiredJwtException e) {
            log.debug("만료된 JWT 토큰입니다.");
//            throw new RequestException(ErrorCode.JWT_UNAUTHORIZED_401);
            return false;
        }
    }
}
