package hanghae.api.coupteambe.domain.repository.document;

import hanghae.api.coupteambe.domain.entity.document.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document, UUID> {
    long countAllByfolder_Id(UUID folderId);
}
