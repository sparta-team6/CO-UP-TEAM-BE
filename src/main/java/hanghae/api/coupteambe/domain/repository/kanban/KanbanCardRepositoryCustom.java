package hanghae.api.coupteambe.domain.repository.kanban;

import hanghae.api.coupteambe.domain.entity.kanban.KanbanCard;

import java.util.List;
import java.util.UUID;

public interface KanbanCardRepositoryCustom {
    List<KanbanCard> findCardsByKanbanBucket_Id_DSL(UUID kanbanBucketId);
}
