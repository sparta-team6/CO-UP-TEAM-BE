package com.hanghae.coupteambe.api.service.sociallogin;

import com.hanghae.coupteambe.api.enumerate.Social;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class SocialLoginServiceMap extends HashMap<String, SocialLoginService> {

    public SocialLoginServiceMap() {
        this.put(Social.GOOGLE.getSocial(), new AuthGoogleService());
        this.put(Social.KAKAO.getSocial(), new AuthKakaoService());
        this.put(Social.NAVER.getSocial(), new AuthNaverService());
    }
}
