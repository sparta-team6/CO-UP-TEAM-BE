package hanghae.api.coupteambe.domain.entity.document;

import hanghae.api.coupteambe.domain.entity.document.Document;
import hanghae.api.coupteambe.domain.entity.document.Folder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    public DocumentDto(Document document) {
        this.docId = document.getId().toString();
        this.title = document.getTitle();
        this.contents = document.getContents();
        this.position = document.getPosition();
    }


}