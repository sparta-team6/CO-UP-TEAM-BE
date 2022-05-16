package hanghae.api.coupteambe.domain.dto.notice;

import hanghae.api.coupteambe.domain.entity.notice.Notice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeInfoDto {

    // 공지사항 번호
    private UUID noticeId;

    // 프로젝트 멤버 ID
    private UUID pjMbId;

    // 공지사항 제목
    private String title;

    // 공지사항 본문
    private String contents;

    public NoticeInfoDto(Notice notice) {
        this.noticeId = notice.getId();
        this.pjMbId = notice.getProjectMember().getId();
        this.title = notice.getTitle();
        this.contents = notice.getContents();
    }
}
