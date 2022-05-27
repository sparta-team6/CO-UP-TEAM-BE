package com.hanghae.coupteambe.api.domain.repository.kanban;

import com.hanghae.coupteambe.api.domain.entity.kanban.KanbanCard;
import com.hanghae.coupteambe.api.enumerate.StatusFlag;
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
