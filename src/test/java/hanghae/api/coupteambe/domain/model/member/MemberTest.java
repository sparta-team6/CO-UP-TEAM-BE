package hanghae.api.coupteambe.domain.model.member;

import hanghae.api.coupteambe.domain.entity.member.Member;
import hanghae.api.coupteambe.domain.repository.MemberRepository;
import hanghae.api.coupteambe.enumerate.Social;
import hanghae.api.coupteambe.enumerate.StatusFlag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MemberTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void createMember(){
        Member member = Member.builder()
                              .loginId("loginId")
                              .nickname("nickname")
                              .password("password")
                              .aboutMe("aboutMe")
                              .profileImage("profileImage")
                              .social(Social.KAKAO)
                              .url("url")
                              .build();

        Member findMember = memberRepository.save(member);

        assertThat(member.getId()).isEqualTo(findMember.getId());
        assertThat(findMember.getDelFlag()).isEqualTo(StatusFlag.SURVIVAL);
        assertThat(findMember.getSocial()).isEqualTo(Social.KAKAO);
    }
}