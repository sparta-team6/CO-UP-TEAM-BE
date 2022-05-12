package hanghae.api.coupteambe.domain.repository.project;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProjectMemberRepositoryImpl implements ProjectMemberRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
}
