package hanghae.api.coupteambe.domain.repository.project;

import hanghae.api.coupteambe.domain.entity.project.ProjectMember;

import java.util.UUID;

public interface ProjectMemberRepositoryCustom {

    // 프로젝트 ID 와 멤버 ID 로 프로젝트 멤버 ID 찾기
    ProjectMember findProjectMemberFromProjectMemberByPjIdAndMbId_DSL(UUID pjId, UUID mbId);

}
