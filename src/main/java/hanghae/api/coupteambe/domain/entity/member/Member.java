package hanghae.api.coupteambe.domain.entity.member;

import hanghae.api.coupteambe.domain.entity.baseentity.BaseEntity;
import hanghae.api.coupteambe.enumerate.Role;
import hanghae.api.coupteambe.enumerate.Social;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Getter
@NoArgsConstructor
@ToString(callSuper = true)
public class Member extends BaseEntity {


    private String memberName;

    private String loginId;

    @Enumerated(EnumType.STRING)
    private Social social;

    private String password;

    private String nickname;

    private String url;

    private String aboutMe;

    private String profileImage;

    private LocalDateTime loginTime;

    private LocalDateTime logoutTime;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Member(String memberName, String loginId, Social social, String password, String nickname, String url,
            String aboutMe, String profileImage, Role role) {
        this.memberName = memberName;
        this.loginId = loginId;
        this.social = social;
        this.password = password;
        this.nickname = nickname;
        this.url = url;
        this.aboutMe = aboutMe;
        this.profileImage = profileImage;
        this.role = role;
    }

    public Member(String loginId, String password, Collection<? extends GrantedAuthority> authorities) {
        this.loginId = loginId;
        this.password = password;
    }
}
