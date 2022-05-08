package hanghae.api.coupteambe.domain.repository;

import hanghae.api.coupteambe.domain.entity.member.Member;
import hanghae.api.coupteambe.enumerate.Social;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {

    Optional<Member> findMemberByLoginIdAndSocial(String loginId, Social social);

    boolean existsMemberByLoginIdAndSocial(String loginId, Social social);

}
