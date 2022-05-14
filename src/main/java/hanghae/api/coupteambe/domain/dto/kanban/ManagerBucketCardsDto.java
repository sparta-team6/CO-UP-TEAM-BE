package hanghae.api.coupteambe.domain.dto.kanban;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Builder
public class ManagerBucketCardsDto {

    private String loginId;

    private String profileImage;

    private String nickname;

    private List<BucketDto> buckets;

    public ManagerBucketCardsDto(String loginId, String profileImage, String nickname, List<BucketDto> buckets) {
        this.loginId = loginId;
        this.profileImage = profileImage;
        this.nickname = nickname;
        this.buckets = buckets;
    }
}
