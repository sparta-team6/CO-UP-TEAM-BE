package com.hanghae.coupteambe.api.domain.repository.project;

import com.hanghae.coupteambe.api.domain.entity.project.ProjectMember;
import com.hanghae.coupteambe.api.domain.entity.project.QProjectMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
