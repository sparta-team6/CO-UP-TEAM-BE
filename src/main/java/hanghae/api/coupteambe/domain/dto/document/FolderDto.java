package hanghae.api.coupteambe.domain.dto.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FolderDto {

    //폴더번호
    private String pjId;
    //폴더이름
    private String title;
    //배치순서
    private int position;

    private List<DocumentDto> docs;
}
