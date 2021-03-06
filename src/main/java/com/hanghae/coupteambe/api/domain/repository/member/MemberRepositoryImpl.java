package com.hanghae.coupteambe.api.domain.repository.member;

import com.hanghae.coupteambe.api.domain.dto.member.ResMemberInfoDto;
import com.hanghae.coupteambe.api.domain.entity.member.QMember;
import com.hanghae.coupteambe.api.domain.entity.project.QProjectMember;
import com.hanghae.coupteambe.api.enumerate.StatusFlag;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
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

//         SELECT m.*
//         FROM project_member pm
//            LEFT OUTER JOIN member m
//                ON (m.id = pm.mb_id)
//         WHERE pm.pj_id = '프로젝트 ID(PK)';
//         -- WHERE pm.pj_id = :pjId

        return jpaQueryFactory
                .select(Projections.constructor(
                        ResMemberInfoDto.class,
                        member.loginId,
                        member.social,
                        member.nickname,
                        member.url,
                        member.aboutMe,
                        member.profileImage))
                .from(projectMember)
                .leftJoin(member)
                .on(member.id.eq(projectMember.member.id))
                .where(projectMember.project.id.eq(pjId)
                        .and(projectMember.delFlag.eq(StatusFlag.NORMAL)))
                .orderBy(projectMember.createdTime.asc())
                .distinct().fetch();
    }
}
