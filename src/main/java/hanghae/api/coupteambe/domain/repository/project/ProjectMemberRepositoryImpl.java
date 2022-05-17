package hanghae.api.coupteambe.domain.repository.project;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hanghae.api.coupteambe.domain.entity.project.ProjectMember;
import hanghae.api.coupteambe.domain.entity.project.QProjectMember;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class ProjectMemberRepositoryImpl implements ProjectMemberRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public ProjectMember findProjectMemberFromProjectMemberByPjIdAndMbId_DSL(UUID pjId, UUID mbId) {
        QProjectMember projectMember = QProjectMember.projectMember;

        return jpaQueryFactory.selectFrom(projectMember)
                .where(projectMember.member.id.eq(mbId).and(projectMember.project.id.eq(pjId)))
                .fetchOne();
    }
}
