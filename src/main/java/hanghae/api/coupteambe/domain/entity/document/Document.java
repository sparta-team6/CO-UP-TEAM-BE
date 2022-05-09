package hanghae.api.coupteambe.domain.entity.document;

import hanghae.api.coupteambe.domain.entity.baseentity.BaseEntity;
import hanghae.api.coupteambe.domain.entity.project.Project;
import hanghae.api.coupteambe.enumerate.MStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;

@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Document extends BaseEntity {

    @Column(updatable = false, nullable = false)
    private String docId;

    @Column(updatable = false, nullable = false)
    private Folder dfId;

    @Column(updatable = false, nullable = false)
    private Project pjId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private int position;

    @Enumerated(EnumType.STRING)
    private MStatus mStatus = MStatus.M_STATUS_SUCCESS;

    @Builder
    public Document(String docId, Folder dfId, Project pjId, String title, String contents, int position, MStatus mStatus) {
        this.docId = docId;
        this.dfId = dfId;
        this.pjId = pjId;
        this.title = title;
        this.contents = contents;
        this.position = position;
        this.mStatus = mStatus;
    }
}
