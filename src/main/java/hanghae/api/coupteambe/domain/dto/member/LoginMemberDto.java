package hanghae.api.coupteambe.domain.dto.member;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class LoginMemberDto {

    @NotNull
    private String loginId;

    @NotNull
    private String password;
}
