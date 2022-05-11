package hanghae.api.coupteambe.domain.repository.project;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hanghae.api.coupteambe.domain.entity.project.Project;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Project> findProjectsFromMemberByLoginId(String loginId) {
        return null;
    }

}
