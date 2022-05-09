package hanghae.api.coupteambe.domain.dto.document;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class FolderDto {

    //폴더번호
    private String dfId;
    //폴더이름
    private String title;
    //배치순서
    private int position;

    private List<DocumentDto> docs;
}
