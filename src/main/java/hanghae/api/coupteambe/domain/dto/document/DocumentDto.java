package hanghae.api.coupteambe.domain.dto.document;

import javax.validation.constraints.NotNull;

public class DocumentDto {

    //문서번호
    private String docId;
    //제목
    private String title;
    //본문
    private String contents;
    //배치순서
    private String position;
    //편집상태값
    private String mStatus;
    //작성자
    private String creator;
    //최종편집자
    private String field2;
    //생성시간
    private String createdTime;
    //수정시간
    private String modifiedTime;
    //삭제여부
    private String delFlag;
    //삭제자
    private String eliminator;
}
