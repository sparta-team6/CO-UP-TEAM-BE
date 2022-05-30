package com.hanghae.coupteambe.api.domain.repository.notice;

import com.hanghae.coupteambe.api.domain.dto.notice.NoticeInfoDto;
import com.hanghae.coupteambe.api.domain.entity.notice.QNotice;
import com.hanghae.coupteambe.api.domain.entity.project.QProjectMember;
import com.hanghae.coupteambe.api.enumerate.StatusFlag;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
                        notice.contents,
                        notice.createdTime,
                        notice.modifiedTime))
                .from(notice)
                .leftJoin(projectMember)
                .on(projectMember.id.eq(notice.projectMember.id))
                .fetchJoin()
                .where(projectMember.project.id.eq(pjId)
                        .and(notice.delFlag.eq(StatusFlag.NORMAL)))
                .orderBy(notice.modifiedTime.desc())
                .distinct().fetch();
    }

}
