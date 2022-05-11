package hanghae.api.coupteambe.domain.repository.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hanghae.api.coupteambe.domain.entity.member.Member;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<Member> findMembersFromProjectByProjectId_DSL(String projectId) {
        return null;
    }
}
