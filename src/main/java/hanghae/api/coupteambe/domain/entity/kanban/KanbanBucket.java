package hanghae.api.coupteambe.domain.entity.kanban;

import hanghae.api.coupteambe.domain.entity.baseentity.BaseEntity;
import hanghae.api.coupteambe.domain.entity.project.Project;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class KanbanBucket extends BaseEntity {

    // 버킷 번호
    @Id
    @Column(name = "KBB_ID", nullable = false, updatable = false)
    private String kbbId;

    // 프로젝트 번호
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PJ_ID", nullable = false, updatable = false)
    private Project pjId;

    // 보드 번호
    @Column(name = "KB_ID", nullable = false)
    private String kbId;

    // 버킷 제목
    @Column(nullable = false)
    private String title;

    // 삭제자
    private String eliminator;

    // 배치순서
    @Column(nullable = false)
    private int position;

    @Builder
    public KanbanBucket(String kbbId, Project pjId, String kbId, String title, String eliminator, int position) {
        this.kbbId = kbbId;
        this.pjId = pjId;
        this.kbId = kbId;
        this.title = title;
        this.eliminator = eliminator;
        this.position = position;
    }
}
