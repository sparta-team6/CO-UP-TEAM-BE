package hanghae.api.coupteambe.domain.entity.document;

import hanghae.api.coupteambe.domain.entity.document.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FolderInfoDto {

    // 소속 프로젝트 ID
    private String pjId;

    // 폴더 ID
    private String dfId;

    // 폴더 제목
    private String title;

    // 배치 순서
    private int position;


}
