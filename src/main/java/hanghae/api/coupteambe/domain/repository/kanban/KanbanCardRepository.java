package hanghae.api.coupteambe.domain.repository.kanban;

import hanghae.api.coupteambe.domain.entity.kanban.KanbanCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface KanbanCardRepository extends JpaRepository<KanbanCard, UUID> {

    List<KanbanCard> findCardsByKanbanBucketIdAndPositionGreaterThanEqual(UUID bucketId, int position);

    long countAllByKanbanBucket_Id(UUID bucketId);
}
