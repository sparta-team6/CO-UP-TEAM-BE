package hanghae.api.coupteambe.domain.dto.document;

import hanghae.api.coupteambe.domain.entity.document.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentDto {
    //폴더번호
    private UUID dfId;
    //문서번호
    private UUID docId;
    //제목
    private String title;
    //본문
    private String contents;
    //배치순서
    private int position;
    //생성시간
    private LocalDateTime createdTime;
    //수정시간
    private LocalDateTime modifiedTime;

    public DocumentDto(Document document) {
        this.docId = document.getId();
        this.title = document.getTitle();
        this.contents = document.getContents();
        this.position = document.getPosition();
        this.createdTime = document.getCreatedTime();
        this.modifiedTime = document.getModifiedTime();
    }


}