package hanghae.api.coupteambe.domain.dto.member;

import hanghae.api.coupteambe.enumerate.Social;

public class ResMemberInfoDto {

    // 로그인 아이디 (email) *소셜 회원가입 시 이메일 중복가입 불허인 관계로, 로그인 ID는 유니크함
    private String loginId;

    // 가입 경로 (소셜: 구글, 카카오, 깃헙)
    private Social social;

    // 닉네임
    private String nickname;

    // 깃허브, 블로그 등 주소(URL)
    private String url;

    // 자기소개
    private String aboutMe;

    // 프로필 이미지 URL
    private String profileImage;
}
