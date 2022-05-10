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
public class KanbanCard extends BaseEntity {

    // 카드 번호
    @Id
    @Column(name = "KBC_ID", updatable = false)
    private String kbcId;

    // 버킷 번호
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "KBB_ID", updatable = false)
    private KanbanBucket kanbanBucket;

    // 프로젝트 번호
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PJ_ID", updatable = false)
    private Project project;

    // 카드 제목
    @Column(nullable = false)
    private String title;

    // 본문
    @Column(nullable = false)
    private String contents;

    // 삭제자
    private String eliminator;

    // 배치순서
    @Column(nullable = false)
    private int position;

    @Builder
    public KanbanCard(String kbcId, KanbanBucket kanbanBucket, Project project, String title, String contents,
                      String eliminator, int position) {
        this.kbcId = kbcId;
        this.kanbanBucket = kanbanBucket;
        this.project = project;
        this.title = title;
        this.contents = contents;
        this.eliminator = eliminator;
        this.position = position;
    }
}
