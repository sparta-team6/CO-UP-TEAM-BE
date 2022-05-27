package com.hanghae.coupteambe.api.domain.repository.kanban;

import com.hanghae.coupteambe.api.domain.entity.kanban.KanbanCard;
import com.hanghae.coupteambe.api.domain.entity.kanban.QKanbanCard;
import com.hanghae.coupteambe.api.domain.entity.member.QMember;
import com.hanghae.coupteambe.api.enumerate.StatusFlag;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
