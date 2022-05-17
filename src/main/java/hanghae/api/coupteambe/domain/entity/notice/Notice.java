package hanghae.api.coupteambe.domain.entity.notice;

import hanghae.api.coupteambe.domain.dto.notice.NoticeInfoDto;
import hanghae.api.coupteambe.domain.entity.baseentity.BaseEntity;
import hanghae.api.coupteambe.domain.entity.project.ProjectMember;
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
    @Column(nullable = false, length = 20)
    @ColumnDefault("'untitle'")
    @Builder.Default
    private String title = "untitle";

    // 본문
    @Column(nullable = false, columnDefinition = "TEXT")
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
