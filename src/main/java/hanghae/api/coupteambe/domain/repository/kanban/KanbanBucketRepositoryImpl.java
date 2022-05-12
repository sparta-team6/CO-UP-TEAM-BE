package hanghae.api.coupteambe.domain.repository.kanban;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hanghae.api.coupteambe.domain.entity.kanban.KanbanBucket;
import hanghae.api.coupteambe.domain.entity.kanban.QKanbanBucket;
import hanghae.api.coupteambe.domain.entity.kanban.QKanbanCard;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class KanbanBucketRepositoryImpl implements KanbanBucketRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<KanbanBucket> findBucketsAndCardsByProject_Id_DSL(String projectId) {
        QKanbanBucket bucket = QKanbanBucket.kanbanBucket;
        QKanbanCard card = QKanbanCard.kanbanCard;

        return jpaQueryFactory.select(bucket)
                              .from(bucket)
                              .innerJoin(bucket.cards, card)
                              .fetchJoin()
                              .where(bucket.project.id.eq(UUID.fromString(projectId)))
                              .distinct().fetch();
    }
}
