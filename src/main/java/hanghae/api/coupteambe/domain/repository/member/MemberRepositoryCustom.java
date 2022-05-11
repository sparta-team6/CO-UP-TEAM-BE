package hanghae.api.coupteambe.domain.repository.member;

import hanghae.api.coupteambe.domain.entity.member.Member;

import java.util.List;

public interface MemberRepositoryCustom {

    //XXX QueryDSL 을 이용한 메소드는 '_DSL' 을 접미사로 붙여주세요.
    // 예, [void updateEntityById_DSL]

    /**
     * 프로젝트에 참가한 멤버들 조회
     */
    List<Member> findMembersFromProjectByProjectId_DSL(String projectId);

}
