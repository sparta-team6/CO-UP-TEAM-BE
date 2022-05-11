package hanghae.api.coupteambe.domain.repository.project;

import hanghae.api.coupteambe.domain.entity.project.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, UUID>, ProjectMemberRepositoryCustom {

}
