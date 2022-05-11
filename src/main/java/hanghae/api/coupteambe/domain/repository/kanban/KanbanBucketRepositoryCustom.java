package hanghae.api.coupteambe.domain.repository.kanban;

import hanghae.api.coupteambe.domain.entity.kanban.KanbanBucket;

import java.util.List;

public interface KanbanBucketRepositoryCustom {

    //XXX QueryDSL 을 이용한 메소드는 '_DSL' 을 접미사로 붙여주세요.
    // 예, [void updateEntityById_DSL]

    List<KanbanBucket> findBucketsAndCardsByProject_Id_DSL(String projectId);
}
