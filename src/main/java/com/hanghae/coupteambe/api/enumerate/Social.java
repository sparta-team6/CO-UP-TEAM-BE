package com.hanghae.coupteambe.api.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Social {

    KAKAO("kakao"),
    NAVER("naver"),
    GOOGLE("google");

    private final String social;
}
