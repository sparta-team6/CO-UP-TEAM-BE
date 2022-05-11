package hanghae.api.coupteambe.domain.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginMemberDto {

    @NotNull
    // 로그인 아이디 (email) *소셜 회원가입 시 이메일 중복가입 불허인 관계로, 로그인 ID는 유니크함
    private String loginId;

    @NotNull
    // 패스워드
    private String password;
}
