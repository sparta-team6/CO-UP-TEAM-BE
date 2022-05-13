package hanghae.api.coupteambe.domain.repository.project;

import hanghae.api.coupteambe.domain.dto.project.ResProjectInfoDto;

import java.util.List;

public interface ProjectRepositoryCustom {

    //XXX QueryDSL 을 이용한 메소드는 '_DSL' 을 접미사로 붙여주세요.
    // 예, [void updateEntityById_DSL]

    /**
     * 멤버가 참가한 프로젝트들 조회
     */
    List<ResProjectInfoDto> findProjectsFromMemberByLoginId_DSL(String loginId);
}
