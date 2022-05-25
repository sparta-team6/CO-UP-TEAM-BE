package hanghae.api.coupteambe.domain.repository.kanban;

import hanghae.api.coupteambe.domain.entity.kanban.KanbanCard;
import hanghae.api.coupteambe.enumerate.StatusFlag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface KanbanCardRepository extends JpaRepository<KanbanCard, UUID>, KanbanCardRepositoryCustom {

    List<KanbanCard> findCardsByKanbanBucketIdAndPositionGreaterThanEqualAndDelFlag(UUID bucketId, int position,
            StatusFlag statusFlag);

    long countAllByKanbanBucket_IdAndDelFlag(UUID bucketId, StatusFlag statusFlag);

    Optional<KanbanCard> findByIdAndDelFlag(UUID cardId, StatusFlag statusFlag);
}
