package hanghae.api.coupteambe.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtTokenDto {

    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiresIn;
}