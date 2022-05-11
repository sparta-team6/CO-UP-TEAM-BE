package hanghae.api.coupteambe.domain.dto.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}