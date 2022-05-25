package hanghae.api.coupteambe.domain.repository.kanban;

import hanghae.api.coupteambe.domain.entity.kanban.KanbanBucket;
import hanghae.api.coupteambe.enumerate.StatusFlag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface KanbanBucketRepository extends JpaRepository<KanbanBucket, UUID>, KanbanBucketRepositoryCustom {
    long countAllByproject_IdAndDelFlag(UUID projectId, StatusFlag statusFlag);

    Optional<KanbanBucket> findByIdAndDelFlag(UUID dstBucketId, StatusFlag normal);
}
