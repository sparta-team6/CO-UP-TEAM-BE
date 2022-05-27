package com.hanghae.coupteambe.api.domain.entity.notice;

import com.hanghae.coupteambe.api.domain.dto.notice.NoticeInfoDto;
import com.hanghae.coupteambe.api.domain.entity.baseentity.BaseEntity;
import com.hanghae.coupteambe.api.domain.entity.project.ProjectMember;
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
public class Notice extends BaseEntity {

    // 제목
    @Column(nullable = false, length = 255)
    @ColumnDefault("'untitle'")
    @Builder.Default
    private String title = "untitle";

    // 본문
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String contents;

    // 프로젝트 멤버 번호
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pjMbId")
    private ProjectMember projectMember;

    public void updateNotice(NoticeInfoDto noticeInfoDto) {
        this.title = noticeInfoDto.getTitle();
        this.contents = noticeInfoDto.getContents();
    }

}
