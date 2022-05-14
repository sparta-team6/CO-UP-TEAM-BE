package hanghae.api.coupteambe.domain.repository.kanban;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hanghae.api.coupteambe.domain.dto.kanban.BucketDto;
import hanghae.api.coupteambe.domain.dto.kanban.CardInfoDto;
import hanghae.api.coupteambe.domain.dto.kanban.ManagerBucketCardsDto;
import hanghae.api.coupteambe.domain.entity.kanban.KanbanBucket;
import hanghae.api.coupteambe.domain.entity.kanban.QKanbanBucket;
import hanghae.api.coupteambe.domain.entity.kanban.QKanbanCard;
import hanghae.api.coupteambe.domain.entity.member.QMember;
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

    @Override
    public List<ManagerBucketCardsDto> findManagersBucketsByProject_Id_DSL(String projectId) {

        QKanbanBucket bucket = QKanbanBucket.kanbanBucket;
        QKanbanCard card = QKanbanCard.kanbanCard;
        QMember member = QMember.member;

        return jpaQueryFactory.select(Projections.fields(
                                      ManagerBucketCardsDto.class,
                                      member.loginId, member.profileImage, member.nickname,
                                      Projections.list(Projections.fields(
                                                      BucketDto.class,
                                                      bucket.title, bucket.position,
                                                      Projections.list(Projections.fields(CardInfoDto.class,
                                                              card.title, card.contents, card.position).as("cards")
                                                      )
                                              ).as("buckets")
                                      )
                              )).from(bucket)
                              .innerJoin(bucket.cards, card).on(bucket.id.eq(card.kanbanBucket.id))
                              .innerJoin(member).on(member.loginId.eq(card.manager)).fetchJoin()
                              .where(bucket.project.id.eq(UUID.fromString(projectId)))
                              .fetch();
    }
}
