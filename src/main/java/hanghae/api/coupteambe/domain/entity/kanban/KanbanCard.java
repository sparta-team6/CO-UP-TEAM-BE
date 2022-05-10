package hanghae.api.coupteambe.domain.entity.kanban;

import hanghae.api.coupteambe.domain.entity.baseentity.BaseEntity;
import hanghae.api.coupteambe.domain.entity.project.Project;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class KanbanCard extends BaseEntity {

    // 버킷 번호
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kbbId")
    private KanbanBucket kanbanBucket;

    // 프로젝트 번호
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pjId")
    private Project project;

    // 카드 제목
    @Column(nullable = false, length = 20)
    @ColumnDefault("'untitle'")
    private String title;

    // 본문
    @Column(nullable = false, columnDefinition = "TEXT")
    private String contents;

    // 배치순서
    @Column(nullable = false)
    @ColumnDefault("0")
    private int position = 0;
}