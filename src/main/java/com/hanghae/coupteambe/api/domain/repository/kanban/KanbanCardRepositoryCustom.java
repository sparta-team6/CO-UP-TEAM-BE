package com.hanghae.coupteambe.api.domain.repository.kanban;

import com.hanghae.coupteambe.api.domain.entity.kanban.KanbanCard;

import java.util.List;
import java.util.UUID;

public interface KanbanCardRepositoryCustom {
    List<KanbanCard> findCardsByKanbanBucket_Id_DSL(UUID kanbanBucketId);
}
