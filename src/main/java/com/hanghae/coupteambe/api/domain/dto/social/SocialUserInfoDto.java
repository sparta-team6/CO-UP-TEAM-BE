package com.hanghae.coupteambe.api.domain.dto.social;

import com.hanghae.coupteambe.api.enumerate.Social;
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
