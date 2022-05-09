package hanghae.api.coupteambe.domain.entity.document;

import hanghae.api.coupteambe.domain.entity.baseentity.BaseEntity;
import hanghae.api.coupteambe.domain.entity.project.Project;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;

@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class Folder extends BaseEntity {

    @Column(updatable = false, nullable = false)
    private String dfId;

    @Column(updatable = false, nullable = false)
    private Project pjId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int position;

    @Column(nullable = false)
    private String creator;

    @Builder
    public Folder(String dfId, Project pjId, String title, int position, String creator) {
        this.dfId = dfId;
        this.pjId = pjId;
        this.title = title;
        this.position = position;
        this.creator = creator;
    }
}
