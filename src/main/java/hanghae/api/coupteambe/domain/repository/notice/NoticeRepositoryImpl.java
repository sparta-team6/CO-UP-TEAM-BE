package hanghae.api.coupteambe.domain.repository.notice;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hanghae.api.coupteambe.domain.dto.notice.NoticeInfoDto;
import hanghae.api.coupteambe.domain.entity.notice.QNotice;
import hanghae.api.coupteambe.domain.entity.project.QProjectMember;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class NoticeRepositoryImpl implements NoticeRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<NoticeInfoDto> findNoticesFromProjectByProjectMbId_DSL(UUID pjMbId) {
        QProjectMember projectMember = QProjectMember.projectMember;
        QNotice notice = QNotice.notice;

        return jpaQueryFactory
                .select(Projections.constructor(
                        NoticeInfoDto.class,
                        notice.id,
                        notice.projectMember.id,
                        notice.title,
                        notice.contents))
                .from(notice)
                .leftJoin(projectMember)
                .on(projectMember.id.eq(notice.projectMember.id))
                .fetchJoin()
                .where(notice.projectMember.id.eq(pjMbId))
                .distinct().fetch();
    }
}
