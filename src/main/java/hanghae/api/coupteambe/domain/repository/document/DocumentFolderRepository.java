package hanghae.api.coupteambe.domain.repository.document;

import hanghae.api.coupteambe.domain.entity.document.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DocumentFolderRepository extends JpaRepository<Folder, UUID>, DocumentFolderRepositoryCustom {
    long countAllByproject_Id(UUID projectId);
}
