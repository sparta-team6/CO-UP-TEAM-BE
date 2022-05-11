package hanghae.api.coupteambe.domain.repository.kanban;

import hanghae.api.coupteambe.domain.entity.kanban.KanbanBucket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface KanbanBucketRepository extends JpaRepository<KanbanBucket, UUID>, KanbanBucketRepositoryCustom {

}
