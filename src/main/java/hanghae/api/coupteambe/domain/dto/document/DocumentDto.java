package hanghae.api.coupteambe.domain.dto.document;

import javax.validation.constraints.NotNull;

public class DocumentDto {
    //폴더번호
    private String dfId;
    //문서번호
    private String docId;
    //제목
    private String title;
    //본문
    private String contents;
    //배치순서
    private int position;
}