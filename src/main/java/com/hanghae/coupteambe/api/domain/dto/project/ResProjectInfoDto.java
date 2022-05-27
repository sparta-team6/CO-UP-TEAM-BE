package com.hanghae.coupteambe.api.domain.dto.project;

import com.hanghae.coupteambe.api.domain.entity.project.ProjectMember;
import com.hanghae.coupteambe.api.enumerate.ProjectRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResProjectInfoDto {

    //프로젝트번호
    private UUID pjId;
    // 이미지주소
    private String thumbnail;
    //프로젝트제목
    private String title;
    //프로젝트개요
    private String summary;
    //초대코드
    private String inviteCode;
    // 현재 사용자 권한
    private ProjectRole projectRole;
    //생성시간
    private LocalDateTime createdTime;
    //수정시간
    private LocalDateTime modifiedTime;
    //배치순서
    private int position;

    public ResProjectInfoDto(ProjectMember projectMember) {
        this.pjId = projectMember.getProject().getId();
        this.thumbnail = projectMember.getProject().getThumbnail();
        this.title = projectMember.getProject().getTitle();
        this.summary = projectMember.getProject().getSummary();
        this.inviteCode = projectMember.getProject().getInviteCode();
        this.projectRole = projectMember.getRole();
        this.createdTime = projectMember.getProject().getCreatedTime();
        this.modifiedTime = projectMember.getProject().getModifiedTime();
        this.position = projectMember.getPosition();
    }

    public void updateRole(ProjectRole projectRole) {
        this.projectRole = projectRole;
    }
}