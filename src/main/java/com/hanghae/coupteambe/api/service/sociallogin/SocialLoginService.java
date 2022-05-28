package com.hanghae.coupteambe.api.service.sociallogin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanghae.coupteambe.api.domain.dto.social.SocialUserInfoDto;

public interface SocialLoginService {
    SocialUserInfoDto socialLogin(String code, String state) throws JsonProcessingException;

    String getAccessToken(String code, String state) throws JsonProcessingException;

    SocialUserInfoDto getUserInfo(String accessToken) throws JsonProcessingException;
}
