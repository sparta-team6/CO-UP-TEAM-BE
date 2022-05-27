package com.hanghae.coupteambe.api.domain.repository.project;

import com.hanghae.coupteambe.api.domain.entity.project.ProjectMember;

import java.util.UUID;

public interface ProjectMemberRepositoryCustom {

    // 프로젝트 ID 와 멤버 ID 로 프로젝트 멤버 ID 찾기
    ProjectMember findProjectMemberFromProjectMemberByPjIdAndMbId_DSL(UUID pjId, UUID mbId);

}
