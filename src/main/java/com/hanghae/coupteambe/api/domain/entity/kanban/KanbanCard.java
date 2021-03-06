package com.hanghae.coupteambe.api.domain.entity.kanban;

import com.hanghae.coupteambe.api.domain.dto.kanban.CardInfoDto;
import com.hanghae.coupteambe.api.domain.entity.baseentity.BaseEntity;
import com.hanghae.coupteambe.api.domain.entity.project.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @Column(nullable = false, length = 255)
    @ColumnDefault("'untitle'")
    @Builder.Default
    private String title = "untitle";

    // 본문
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String contents;

    // 담당자
    private String manager;

    private String managerNickname;

    // 배치순서
    @Column(nullable = false)
    @ColumnDefault("0")
    @Builder.Default
    private int position = 0;

    public void updateKanbanCard(CardInfoDto cardInfoDto) {
        this.title = cardInfoDto.getTitle();
        this.contents = cardInfoDto.getContents();
        this.position = cardInfoDto.getPosition();
        this.manager = cardInfoDto.getManager();
        this.managerNickname = cardInfoDto.getManagerNickname();
    }

    public void updateBucket(KanbanBucket kanbanBucket) {
        this.kanbanBucket = kanbanBucket;
    }

    public void plusPosition() {
        this.position += 1;
    }

    public void minusPosition() {
        this.position -= 1;
    }
}
