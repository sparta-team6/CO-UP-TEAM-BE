package hanghae.api.coupteambe.domain.repository.member;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hanghae.api.coupteambe.domain.dto.member.ResMemberInfoDto;
import hanghae.api.coupteambe.domain.entity.member.QMember;
import hanghae.api.coupteambe.domain.entity.project.QProjectMember;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<ResMemberInfoDto> findMembersFromProjectByProjectId_DSL(UUID pjId) {
        QProjectMember projectMember = QProjectMember.projectMember;
        QMember member = QMember.member;

        return jpaQueryFactory
                .select(Projections.constructor(
                        ResMemberInfoDto.class,
                        member.loginId,
                        member.social,
                        member.profileImage,
                        member.url,
                        member.nickname,
                        member.aboutMe))
                .from(projectMember)
                .leftJoin(member)
                .on(member.id.eq(projectMember.member.id))
                .fetchJoin()
                .where(projectMember.project.id.eq(pjId))
                .distinct().fetch();
    }


}
