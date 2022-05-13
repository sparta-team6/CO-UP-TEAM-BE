package hanghae.api.coupteambe.domain.repository.project;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hanghae.api.coupteambe.domain.dto.project.ResProjectInfoDto;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ResProjectInfoDto> findProjectsFromMemberByLoginId_DSL(String loginId) {
        return null;
    }

}
