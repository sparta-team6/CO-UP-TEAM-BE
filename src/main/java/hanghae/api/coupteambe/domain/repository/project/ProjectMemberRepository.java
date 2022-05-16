package hanghae.api.coupteambe.domain.repository.project;

import hanghae.api.coupteambe.domain.entity.member.Member;
import hanghae.api.coupteambe.domain.entity.project.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, UUID>, ProjectMemberRepositoryCustom {
    Optional<ProjectMember> findById(UUID pjMbId);
    boolean existsByMember(Member member);
}
