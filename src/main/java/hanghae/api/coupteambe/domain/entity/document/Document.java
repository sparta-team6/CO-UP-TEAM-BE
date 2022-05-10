package hanghae.api.coupteambe.domain.entity.document;

import hanghae.api.coupteambe.domain.entity.baseentity.BaseEntity;
import hanghae.api.coupteambe.domain.entity.project.Project;
import hanghae.api.coupteambe.enumerate.MStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Document extends BaseEntity {


    @Column(updatable = false, nullable = false)
    private String docId;

    //Document : Folder => N: 1 엔티티에서 dfId 외래키를 뜻함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FOLDERdfId", nullable = false)
    private Folder folder;

    //Document : Project => N: 1 엔티티에서 pjId 외래키를 뜻함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECTpjId", nullable = false)
    private Project project;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private int position;

    @Enumerated(EnumType.STRING)
    private MStatus mStatus = MStatus.M_STATUS_SUCCESS;

    @Builder
    public Document(String docId, Folder folder, Project project, String title, String contents, int position, MStatus mStatus) {
        this.docId = docId;
        this.folder = folder;
        this.project = project;
        this.title = title;
        this.contents = contents;
        this.position = position;
        this.mStatus = mStatus;
    }
}
