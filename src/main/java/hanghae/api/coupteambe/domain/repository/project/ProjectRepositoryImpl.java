package hanghae.api.coupteambe.domain.repository.project;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hanghae.api.coupteambe.domain.dto.project.ResProjectInfoDto;
import hanghae.api.coupteambe.domain.entity.project.QProject;
import hanghae.api.coupteambe.domain.entity.project.QProjectMember;
import hanghae.api.coupteambe.enumerate.StatusFlag;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ResProjectInfoDto> findProjectsFromMemberByLoginId_DSL(UUID mbId) {
        QProject project = QProject.project;
        QProjectMember projectMember = QProjectMember.projectMember;

//        SELECT p.*
//        FROM PROJECT_MEMBER pm
//        LEFT OUTER JOIN PROJECT p
//        ON (p.ID = pm.PJ_ID)
//        WHERE pm.MB_ID = '유저 ID(PK)';
//        -- WHERE pm.MB_ID = :mbId

        return jpaQueryFactory
                .select(Projections.constructor(
                        ResProjectInfoDto.class,
                        project.id,
                        project.thumbnail,
                        project.title,
                        project.summary,
                        project.inviteCode,
                        projectMember.role,
                        project.createdTime,
                        project.modifiedTime,
                        projectMember.position))
                .from(projectMember)
                .leftJoin(project)
                .on(project.id.eq(projectMember.project.id))
                .where(projectMember.member.id.eq(mbId)
                        .and(project.delFlag.eq(StatusFlag.NORMAL)))
                .orderBy(projectMember.position.asc())
                .distinct().fetch();
    }

}
