package hanghae.api.coupteambe.domain.entity.kanban;

import hanghae.api.coupteambe.domain.entity.baseentity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@Getter
@NoArgsConstructor
public class KanbanCard extends BaseEntity {

    // 카드 번호
    private String kbcId;

    // 버킷 번호
    private String kbbId;

    // 프로젝트 번호
    private String pjId;

    // 카드 제목
    private String title;

    // 본문
    private String contents;

    // 개설자
    private String creator;

    // 삭제자
    private String eliminator;

    // 배치순서
    private int position;

    // 최종편집자
    private String editor;

    @Builder
    public KanbanCard(String kbcId, String kbbId, String pjId, String title, String contents, String creator,
                      String eliminator, int position, String editor) {
        this.kbcId = kbcId;
        this.kbbId = kbbId;
        this.pjId = pjId;
        this.title = title;
        this.contents = contents;
        this.creator = creator;
        this.eliminator = eliminator;
        this.position = position;
        this.editor = editor;
    }
}
