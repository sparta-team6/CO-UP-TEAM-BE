package hanghae.api.coupteambe.domain.dto.kanban;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BucketInfoDto {

    // 소속 프로젝트 ID
    private String pjId;

    // 버킷 ID
    private String kbbId;

    // 버킷 제목
    private String title;

    // 배치 순서
    private int position;

}
