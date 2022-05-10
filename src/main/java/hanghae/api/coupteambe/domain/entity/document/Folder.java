package hanghae.api.coupteambe.domain.entity.document;

import hanghae.api.coupteambe.domain.entity.baseentity.BaseEntity;
import hanghae.api.coupteambe.domain.entity.project.Project;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Folder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String dfId;

    //Folder : Project => N: 1 엔티티에서 pjId 외래키를 뜻함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECTpjId", nullable = false)
    private Project project;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int position;

    @Column(nullable = false)
    private String creator;

    @Builder
    public Folder(String dfId, Project project, String title, int position, String creator) {
        this.dfId = dfId;
        this.project = project;
        this.title = title;
        this.position = position;
        this.creator = creator;
    }
}
