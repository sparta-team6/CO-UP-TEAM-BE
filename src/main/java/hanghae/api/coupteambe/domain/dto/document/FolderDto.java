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
    private String position;
    //개설자
    private String creator;
    //생성시간
    private String createdTime;
    //수정시간
    private String modifiedTime;
    //삭제여부
    private String delFlag;
    //삭제여부
    private String eliminator;

    private List<DocumentDto> docs;
}
