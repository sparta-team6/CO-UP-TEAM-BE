package hanghae.api.coupteambe.domain.entity.member;

import hanghae.api.coupteambe.domain.dto.social.SocialUserInfoDto;
import hanghae.api.coupteambe.domain.entity.baseentity.BaseEntity;
import hanghae.api.coupteambe.enumerate.Role;
import hanghae.api.coupteambe.enumerate.Social;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member extends BaseEntity {

    @Column(unique = true, length = 50)
    private String loginId;

    @Enumerated(EnumType.STRING)
    private Social social;

    @Column(nullable = false, length = 20)
    private String password;

    @Column(nullable = false, length = 20)
    private String nickname;

    private String url;

    @Column(columnDefinition = "TEXT")
    private String aboutMe;

    @Column(columnDefinition = "TEXT")
    private String profileImage;

    private LocalDateTime loginTime;

    private LocalDateTime logoutTime;

    @Enumerated(EnumType.STRING)
    private Role role;


    //fixme 소셜로그인 전용임으로 password 불필요하나,
    // 스프링시큐리티 특성상 암호화된 password 를 넣어야한다. updatePassword()를 통해 암호화된 비밀번호를 넣자.
    // 추후 일반 회원가입 기능이 생긴다면, 패스워드 검증 룰을 재정의 하여야한다.

    public Member(SocialUserInfoDto socialUserInfoDto) {
        this.loginId = socialUserInfoDto.getLoginId();
        this.nickname = socialUserInfoDto.getNickname();
        this.profileImage = socialUserInfoDto.getProfileImage();
        this.social = socialUserInfoDto.getSocial();
        this.role = Role.ROLE_USER;
//        this.password = socialUserInfoDto.getLoginId() + socialUserInfoDto.getSocial().toString();
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateLoginTime(LocalDateTime currentTime) {
        this.loginTime = currentTime;
    }

    public void updateLogoutTime(LocalDateTime currentTime) {
        this.logoutTime = currentTime;
    }
}
