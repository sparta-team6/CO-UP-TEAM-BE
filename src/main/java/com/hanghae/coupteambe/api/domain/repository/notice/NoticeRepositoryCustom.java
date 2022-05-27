package com.hanghae.coupteambe.api.domain.repository.notice;

import com.hanghae.coupteambe.api.domain.dto.notice.NoticeInfoDto;

import java.util.List;
import java.util.UUID;

public interface NoticeRepositoryCustom {

    // 프로젝트 내 공지사항 전체 조회
    List<NoticeInfoDto> findAllNoticeByPjId_DSL(UUID pjId);
}
