package hanghae.api.coupteambe.domain.repository.kanban;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hanghae.api.coupteambe.domain.entity.kanban.KanbanBucket;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class KanbanBucketRepositoryImpl implements KanbanBucketRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<KanbanBucket> findBucketsAndCardsByProject_Id_DSL(String projectId) {
        return null;
    }
}
