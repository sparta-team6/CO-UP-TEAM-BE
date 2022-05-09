package hanghae.api.coupteambe.domain.dto.social;

import hanghae.api.coupteambe.enumerate.Social;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SocialUserInfoDto {

    private String loginId;
    private String nickname;
    private String profileImage;
    private Social social;
}
