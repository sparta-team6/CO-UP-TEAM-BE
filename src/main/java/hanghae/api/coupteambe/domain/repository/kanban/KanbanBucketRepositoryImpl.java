package hanghae.api.coupteambe.domain.repository.kanban;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hanghae.api.coupteambe.domain.dto.kanban.BucketDto;
import hanghae.api.coupteambe.domain.dto.kanban.CardInfoDto;
import hanghae.api.coupteambe.domain.dto.kanban.ManagerBucketCardsDto;
import hanghae.api.coupteambe.domain.entity.kanban.KanbanBucket;
import hanghae.api.coupteambe.domain.entity.kanban.QKanbanBucket;
import hanghae.api.coupteambe.domain.entity.kanban.QKanbanCard;
import hanghae.api.coupteambe.domain.entity.member.QMember;
import hanghae.api.coupteambe.domain.entity.project.QProject;
import hanghae.api.coupteambe.domain.entity.project.QProjectMember;
import hanghae.api.coupteambe.enumerate.StatusFlag;
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
                                            .leftJoin(card).on(bucket.eq(card.kanbanBucket))
                                            .leftJoin(member).on(card.manager.eq(member.loginId))
                                            .where(projectMember.project.id.eq(UUID.fromString(projectId))
                                                    .and(bucket.delFlag.eq(StatusFlag.NORMAL))
                                                    .and(card.delFlag.eq(StatusFlag.NORMAL)))
                                            .select(
                                                    card.manager, member.profileImage, member.nickname,
                                                    bucket.id, bucket.title, bucket.position,
                                                    card.id, card.title, card.contents, card.position
                                            )
                                            .orderBy(card.manager.asc(), bucket.position.asc(), card.position.asc())
                                            .fetch();

        HashMap<String, ManagerBucketCardsDto> managerMap = new HashMap<>();
        HashMap<String, BucketDto> bucketMap = new HashMap<>();
        for (Tuple t : result) {

            ManagerBucketCardsDto managerBucketCardsDto = null;

            if (managerMap.containsKey(t.get(member.loginId))) {
                managerBucketCardsDto = managerMap.get(t.get(member.loginId));
            } else {
                managerBucketCardsDto = new ManagerBucketCardsDto(
                        t.get(member.loginId),
                        t.get(member.profileImage),
                        t.get(member.nickname)
                );
                managerMap.put(t.get(member.loginId), managerBucketCardsDto);
            }

            BucketDto bucketDto = null;
            if (bucketMap.containsKey(t.get(bucket.id).toString() + t.get(member.loginId))) {
                bucketDto = bucketMap.get(t.get(bucket.id).toString() + t.get(member.loginId));
            } else {
                bucketDto = new BucketDto(
                        t.get(bucket.id),
                        t.get(bucket.title),
                        t.get(bucket.position));
                bucketMap.put(t.get(bucket.id).toString() + t.get(member.loginId), bucketDto);

                managerBucketCardsDto.addBucket(bucketDto);
            }

            CardInfoDto cardInfoDto = new CardInfoDto(
                    t.get(card.id),
                    t.get(bucket.id),
                    t.get(member.loginId),
                    t.get(card.title),
                    t.get(card.contents),
                    t.get(card.position)
            );

            bucketDto.addCard(cardInfoDto);
        }

        return new ArrayList<>(managerMap.values());
    }
}
