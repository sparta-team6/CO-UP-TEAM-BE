package hanghae.api.coupteambe.domain.dto.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FolderInfoDto {

    // 소속 프로젝트 ID
    private UUID pjId;

    // 폴더 ID
    private UUID dfId;

    // 폴더 제목
    private String title;

    // 배치 순서
    private int position;


}
