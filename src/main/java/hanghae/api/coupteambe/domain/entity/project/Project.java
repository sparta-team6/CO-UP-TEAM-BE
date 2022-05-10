package hanghae.api.coupteambe.domain.entity.project;

import hanghae.api.coupteambe.domain.entity.baseentity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Getter
@NoArgsConstructor
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String pjId;

    private String thumbnail;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String summary;

    @Column(nullable = false)
    private String inviteCode;

    @Column(nullable = false)
    private String creator;

    @Builder
    public Project(String pjId, String thumbnail, String title, String summary, String inviteCode, String creator) {
        this.pjId = pjId;
        this.thumbnail = thumbnail;
        this.title = title;
        this.summary = summary;
        this.inviteCode = inviteCode;
        this.creator = creator;
    }
}
