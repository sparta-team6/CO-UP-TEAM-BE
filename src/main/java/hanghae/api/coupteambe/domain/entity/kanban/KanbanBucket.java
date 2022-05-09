package hanghae.api.coupteambe.domain.entity.kanban;

import hanghae.api.coupteambe.domain.entity.baseentity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@Getter
@NoArgsConstructor
public class KanbanBucket extends BaseEntity {

    // 버킷 번호
    private String kbbId;

    // 프로젝트 번호
    private String pjId;

    // 보드 번호
    private String kbId;

    // 버킷 제목
    private String title;

    // 개설자
    private String creator;

    // 삭제자
    private String eliminator;

    // 배치순서
    private int position;

    // 최종편집자
    private String editor;

    @Builder
    public KanbanBucket(String kbbId, String pjId, String kbId, String title, String creator, String eliminator,
                        int position, String editor) {
        this.kbbId = kbbId;
        this.pjId = pjId;
        this.kbId = kbId;
        this.title = title;
        this.creator = creator;
        this.eliminator = eliminator;
        this.position = position;
        this.editor = editor;
    }
}
