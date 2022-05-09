package hanghae.api.coupteambe.domain.dto.document;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class FolderDto {

    private String dfId;
    private String title;
    private String position;
    private String creator;
    private String createdTime;
    private String modifiedTime;
    private String delFlag;
    private String eliminator;

    private List<DocumentDto> docs;
}
