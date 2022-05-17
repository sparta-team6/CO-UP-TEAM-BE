package hanghae.api.coupteambe.domain.dto.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FolderDto {

    // 프로젝트 ID
    private UUID pjId;

    // 폴더 ID
    private String dfId;

    //폴더이름
    private String title;

    //배치순서
    private int position;

    private List<DocumentDto> docs;


}
