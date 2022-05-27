package com.hanghae.coupteambe.api.domain.repository.kanban;

import com.hanghae.coupteambe.api.domain.entity.kanban.KanbanBucket;
import com.hanghae.coupteambe.api.enumerate.StatusFlag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface KanbanBucketRepository extends JpaRepository<KanbanBucket, UUID>, KanbanBucketRepositoryCustom {
    long countAllByproject_IdAndDelFlag(UUID projectId, StatusFlag statusFlag);

    Optional<KanbanBucket> findByIdAndDelFlag(UUID dstBucketId, StatusFlag normal);
}
