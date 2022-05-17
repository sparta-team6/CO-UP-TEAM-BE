package hanghae.api.coupteambe.domain.repository.project;

import hanghae.api.coupteambe.domain.entity.member.Member;
import hanghae.api.coupteambe.domain.entity.project.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, UUID>, ProjectMemberRepositoryCustom {
    Optional<ProjectMember> findByMember(Member member);
    boolean existsByMember(Member member);
    List<ProjectMember> findByMemberId(UUID mbId);
    Optional<ProjectMember> findByMemberIdAndProjectId(UUID mbId, UUID pjId);
}
