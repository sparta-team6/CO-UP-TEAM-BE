package com.hanghae.coupteambe.api.domain.repository.member;

import com.hanghae.coupteambe.api.domain.dto.member.ResMemberInfoDto;

import java.util.List;
import java.util.UUID;

public interface MemberRepositoryCustom {

    //XXX QueryDSL 을 이용한 메소드는 '_DSL' 을 접미사로 붙여주세요.
    // 예, [void updateEntityById_DSL]

    /**
     * 프로젝트에 참가한 멤버들 조회
     */
    List<ResMemberInfoDto> findMembersFromProjectByProjectId_DSL(UUID pjId);

}
