package com.hanghae.coupteambe.api.domain.repository.project;

import com.hanghae.coupteambe.api.domain.dto.project.ResProjectInfoDto;

import java.util.List;
import java.util.UUID;

public interface ProjectRepositoryCustom {

    //XXX QueryDSL 을 이용한 메소드는 '_DSL' 을 접미사로 붙여주세요.
    // 예, [void updateEntityById_DSL]

    /**
     * 멤버가 참가한 프로젝트들 조회
     */
    List<ResProjectInfoDto> findProjectsFromMemberByLoginId_DSL(UUID mbId);
}
