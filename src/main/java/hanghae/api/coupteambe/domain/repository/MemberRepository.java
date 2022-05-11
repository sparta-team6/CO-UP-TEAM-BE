package hanghae.api.coupteambe.domain.repository;

import hanghae.api.coupteambe.domain.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {

    Optional<Member> findByLoginId(String loginId);

    boolean existsMemberByLoginId(String loginId);

}
