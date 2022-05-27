package com.hanghae.coupteambe.api.domain.dto.member;

import lombok.Getter;

@Getter
public class ReqMemberInfoDto {

    // 로그인ID
    private String loginId;

    // 프로필 이미지 URL
    private String profileImage;

    // 닉네임
    private String nickname;

    // 깃허브, 블로그 등 주소(URL)
    private String url;

    // 자기소개
    private String aboutMe;

}
