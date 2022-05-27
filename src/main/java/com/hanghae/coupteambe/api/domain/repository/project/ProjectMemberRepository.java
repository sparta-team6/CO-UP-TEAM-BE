package com.hanghae.coupteambe.api.domain.repository.project;

import com.hanghae.coupteambe.api.domain.entity.member.Member;
import com.hanghae.coupteambe.api.domain.entity.project.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, UUID>, ProjectMemberRepositoryCustom {
    Optional<ProjectMember> findTop1ByProjectId(UUID pjID);
    Optional<ProjectMember> findByMember(Member member);
    boolean existsByMember(Member member);
    List<ProjectMember> findByMemberId(UUID mbId);
    Optional<ProjectMember> findByMemberIdAndProjectId(UUID mbId, UUID pjId);
    long countAllByMember_Id(UUID memberId);
}
