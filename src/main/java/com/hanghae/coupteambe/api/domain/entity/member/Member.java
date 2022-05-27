package com.hanghae.coupteambe.api.domain.entity.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hanghae.coupteambe.api.domain.dto.member.ReqMemberInfoDto;
import com.hanghae.coupteambe.api.domain.dto.social.SocialUserInfoDto;
import com.hanghae.coupteambe.api.domain.entity.baseentity.BaseEntity;
import com.hanghae.coupteambe.api.domain.entity.project.ProjectMember;
import com.hanghae.coupteambe.api.enumerate.Role;
import com.hanghae.coupteambe.api.enumerate.Social;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseEntity {

    @Column(unique = true, length = 50)
    private String loginId;

    @Enumerated(EnumType.STRING)
    private Social social;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 20)
    private String nickname;

    private String url;

    @Column(columnDefinition = "LONGTEXT")
    private String aboutMe;

    @Column(columnDefinition = "LONGTEXT")
    private String profileImage;

    private LocalDateTime loginTime;

    private LocalDateTime logoutTime;

    @Enumerated(EnumType.STRING)
    private Role role;

    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<ProjectMember> projectMembers = new ArrayList<>();

    @Builder
    public Member(String loginId, Social social, String password, String nickname, String url, String aboutMe,
            String profileImage, LocalDateTime loginTime, LocalDateTime logoutTime, Role role) {
        this.loginId = loginId;
        this.social = social;
        this.password = password;
        this.nickname = nickname;
        this.url = url;
        this.aboutMe = aboutMe;
        this.profileImage = profileImage;
        this.loginTime = loginTime;
        this.logoutTime = logoutTime;
        this.role = role;
    }
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

    public void updateMember(ReqMemberInfoDto reqMemberInfoDto) {

        this.profileImage = reqMemberInfoDto.getProfileImage();
        this.nickname = reqMemberInfoDto.getNickname();
        this.url = reqMemberInfoDto.getUrl();
        this.aboutMe = reqMemberInfoDto.getAboutMe();
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

    /**
     * 객체지향 목적성으로 등록하는 것이며,
     * 프로젝트를 추가하더라도 데이터베이스에 추가되지 않습니다.
     */
    public void addProjects(ProjectMember projectMember) {
        this.projectMembers.add(projectMember);
    }

    /**
     * 객체지향 목적성으로 등록하는 것이며,
     * 프로젝트를 추가하더라도 데이터베이스에 추가되지 않습니다.
     */
    public void removeProject(ProjectMember projectMember) {
        this.projectMembers.remove(projectMember);
    }
}
