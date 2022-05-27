package com.hanghae.coupteambe.api.domain.repository.kanban;

import com.hanghae.coupteambe.api.domain.dto.kanban.BucketDto;
import com.hanghae.coupteambe.api.domain.dto.kanban.CardInfoDto;
import com.hanghae.coupteambe.api.domain.dto.kanban.ManagerBucketCardsDto;
import com.hanghae.coupteambe.api.domain.entity.kanban.KanbanBucket;
import com.hanghae.coupteambe.api.domain.entity.kanban.QKanbanBucket;
import com.hanghae.coupteambe.api.domain.entity.kanban.QKanbanCard;
import com.hanghae.coupteambe.api.domain.entity.member.QMember;
import com.hanghae.coupteambe.api.domain.entity.project.QProject;
import com.hanghae.coupteambe.api.domain.entity.project.QProjectMember;
import com.hanghae.coupteambe.api.enumerate.StatusFlag;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class KanbanBucketRepositoryImpl implements KanbanBucketRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<KanbanBucket> findBucketsByProject_Id_DSL(String projectId) {
        QKanbanBucket bucket = QKanbanBucket.kanbanBucket;

        return jpaQueryFactory.select(bucket)
                .from(bucket)
                .where(bucket.project.id.eq(UUID.fromString(projectId))
                        .and(bucket.delFlag.eq(StatusFlag.NORMAL)))
                .orderBy(bucket.position.asc())
                .distinct().fetch();
    }

    @Override
    public List<ManagerBucketCardsDto> findManagersBucketsByProject_Id_DSL(String projectId) {


        QKanbanBucket bucket = QKanbanBucket.kanbanBucket;
        QKanbanCard card = QKanbanCard.kanbanCard;
        QMember member = QMember.member;
        QProjectMember projectMember = QProjectMember.projectMember;
        QProject project = QProject.project;

//        String projectId = "5526bcb0-5feb-4668-8893-d837083a1bc3";

        List<Tuple> result = jpaQueryFactory.from(projectMember)
                                            .leftJoin(bucket).on(projectMember.project.eq(bucket.project))
                                            .rightJoin(card).on(bucket.eq(card.kanbanBucket))
                                            .leftJoin(member).on(card.manager.eq(member.loginId))
                                            .where(projectMember.project.id.eq(UUID.fromString(projectId))
                                                                           .and(bucket.delFlag.eq(StatusFlag.NORMAL))
                                                                           .and(card.delFlag.eq(StatusFlag.NORMAL)))
                                            .select(
                                                    card.manager, member.profileImage, member.nickname,
                                                    bucket.id, bucket.title, bucket.position,
                                                    card.id, card.title, card.contents, card.position,
                                                    card.managerNickname
                                            ).distinct()
                                            .orderBy(card.manager.asc(), bucket.position.asc(), card.position.asc())
                                            .fetch();

        HashMap<String, ManagerBucketCardsDto> managerMap = new HashMap<>();
        HashMap<String, BucketDto> bucketMap = new HashMap<>();
        for (Tuple t : result) {

            ManagerBucketCardsDto managerBucketCardsDto = null;

            if (managerMap.containsKey(t.get(card.manager))) {
                managerBucketCardsDto = managerMap.get(t.get(card.manager));
            } else {
                managerBucketCardsDto = new ManagerBucketCardsDto(
                        t.get(card.manager),
                        t.get(member.profileImage),
                        t.get(member.nickname)
                );
                managerMap.put(t.get(card.manager), managerBucketCardsDto);
            }

            BucketDto bucketDto = null;
            if (bucketMap.containsKey(t.get(bucket.id).toString() + t.get(card.manager))) {
                bucketDto = bucketMap.get(t.get(bucket.id).toString() + t.get(card.manager));
            } else {
                bucketDto = new BucketDto(
                        t.get(bucket.id),
                        t.get(bucket.title),
                        t.get(bucket.position));
                bucketMap.put(t.get(bucket.id).toString() + t.get(card.manager), bucketDto);

                managerBucketCardsDto.addBucket(bucketDto);
            }

            CardInfoDto cardInfoDto = new CardInfoDto(
                    t.get(card.id),
                    t.get(bucket.id),
                    t.get(card.manager),
                    t.get(card.managerNickname),
                    t.get(card.title),
                    t.get(card.contents),
                    t.get(card.position)
            );

            bucketDto.addCard(cardInfoDto);
        }

        return new ArrayList<>(managerMap.values());
    }
}
