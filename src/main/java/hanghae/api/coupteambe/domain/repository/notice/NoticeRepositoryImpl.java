package hanghae.api.coupteambe.domain.repository.notice;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hanghae.api.coupteambe.domain.dto.notice.NoticeInfoDto;
import hanghae.api.coupteambe.domain.entity.notice.QNotice;
import hanghae.api.coupteambe.domain.entity.project.QProjectMember;
import hanghae.api.coupteambe.enumerate.StatusFlag;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class NoticeRepositoryImpl implements NoticeRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<NoticeInfoDto> findAllNoticeByPjId_DSL(UUID pjId) {
        QProjectMember projectMember = QProjectMember.projectMember;
        QNotice notice = QNotice.notice;

        return jpaQueryFactory
                .select(Projections.constructor(
                        NoticeInfoDto.class,
                        notice.id,
                        projectMember.project.id,
                        projectMember.member.id,
                        notice.title,
                        notice.contents))
                .from(notice)
                .leftJoin(projectMember)
                .on(projectMember.id.eq(notice.projectMember.id))
                .fetchJoin()
                .where(projectMember.project.id.eq(pjId)
                        .and(notice.delFlag.eq(StatusFlag.NORMAL)))
                .distinct().fetch();
    }

}
