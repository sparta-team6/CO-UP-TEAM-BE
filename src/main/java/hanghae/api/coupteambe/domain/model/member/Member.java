package hanghae.api.coupteambe.domain.model.member;

import hanghae.api.coupteambe.domain.model.baseentity.BaseEntity;
import hanghae.api.coupteambe.enumerate.Social;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @Builder
    public Member(String memberName, String loginId, Social social, String password, String nickname, String url,
            String aboutMe, String profileImage) {
        this.memberName = memberName;
        this.loginId = loginId;
        this.social = social;
        this.password = password;
        this.nickname = nickname;
        this.url = url;
        this.aboutMe = aboutMe;
        this.profileImage = profileImage;
    }
}
