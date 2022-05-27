package com.hanghae.coupteambe.api.domain.repository.project;

import com.hanghae.coupteambe.api.domain.entity.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID>, ProjectRepositoryCustom {
    // 초대코드로 프로젝트 찾기
    Optional<Project> findByInviteCode(String inviteCode);
    Optional<Project> findById(UUID pjId);
}
