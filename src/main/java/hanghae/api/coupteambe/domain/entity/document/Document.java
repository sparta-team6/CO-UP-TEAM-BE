package hanghae.api.coupteambe.domain.entity.document;

import hanghae.api.coupteambe.domain.entity.baseentity.BaseEntity;
import hanghae.api.coupteambe.domain.entity.project.Project;
import hanghae.api.coupteambe.enumerate.MStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "DOCUMENTS")
public class Document extends BaseEntity {

    //Document : Folder => N: 1 엔티티에서 dfId 외래키를 뜻함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dfId")
    private Folder folder;

    //Document : Project => N: 1 엔티티에서 pjId 외래키를 뜻함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pjId")
    private Project project;

    @Column(nullable = false, length = 20)
    @ColumnDefault("'untitle'")
    private String title = "untitle";

    @Column(nullable = false, columnDefinition = "TEXT")
    @ColumnDefault("''")
    private String contents = "";

    @Column(nullable = false)
    @ColumnDefault("0")
    private int position = 0;

    @Enumerated(EnumType.STRING)
    private MStatus mStatus = MStatus.M_STATUS_SUCCESS;
}
