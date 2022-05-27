package hanghae.api.coupteambe.domain.repository.kanban;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hanghae.api.coupteambe.domain.entity.kanban.KanbanCard;
import hanghae.api.coupteambe.domain.entity.kanban.QKanbanCard;
import hanghae.api.coupteambe.domain.entity.member.QMember;
import hanghae.api.coupteambe.enumerate.StatusFlag;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class KanbanCardRepositoryImpl implements KanbanCardRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<KanbanCard> findCardsByKanbanBucket_Id_DSL(UUID kanbanBucketId) {
        QKanbanCard card = QKanbanCard.kanbanCard;
        QMember member = QMember.member;

        return jpaQueryFactory.select(card)
                              .from(card)
                              .leftJoin(member).on(card.manager.eq(member.loginId))
                              .where(card.kanbanBucket.id.eq(kanbanBucketId)
                                                         .and(card.delFlag.eq(StatusFlag.NORMAL)))
                              .orderBy(card.position.asc())
                              .distinct().fetch();
    }
}
