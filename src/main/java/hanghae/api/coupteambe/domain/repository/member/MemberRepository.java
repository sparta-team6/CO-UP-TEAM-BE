package hanghae.api.coupteambe.domain.repository.member;

import hanghae.api.coupteambe.domain.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID>, MemberRepositoryCustom {

    /**
     * 검색 키워드 : auditoraware stackoverflow
     * Auditorware 에서 스택오버플로우가 터져서 검색 결과 아래와 같이 하였다.
     * REQIORES_NEW 는 기존 호출메소드의 트랜젝션을 중지하고 새로운 트랜젝션 처리하는 것이다.
     * 오버플로우 원인은 트랜잭션 진행 전후 로직에서 Auditorware 를 호출하게 된다.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    Optional<Member> findRequires_NewByLoginId(String loginId);

    Optional<Member> findByLoginId(String loginId);

    boolean existsMemberByLoginId(String loginId);

}
