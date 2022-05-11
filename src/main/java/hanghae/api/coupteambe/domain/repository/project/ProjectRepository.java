package hanghae.api.coupteambe.domain.repository.project;

import hanghae.api.coupteambe.domain.entity.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID>, ProjectRepositoryCustom {

}
