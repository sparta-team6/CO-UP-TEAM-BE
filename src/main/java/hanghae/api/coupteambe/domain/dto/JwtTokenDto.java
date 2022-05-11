package hanghae.api.coupteambe.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtTokenDto {

    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiresIn;
}