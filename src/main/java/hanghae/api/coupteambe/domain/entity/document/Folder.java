package hanghae.api.coupteambe.domain.entity.document;

import hanghae.api.coupteambe.domain.dto.document.FolderDto;
import hanghae.api.coupteambe.domain.entity.baseentity.BaseEntity;
import hanghae.api.coupteambe.domain.entity.project.Project;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor
public class Folder extends BaseEntity {

    //Folder : Project => N: 1 엔티티에서 pjId 외래키를 뜻함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pjId")
    private Project project;

    @Column(nullable = false, length = 255)
    @ColumnDefault("'untitle'")
    private String title = "title";

    @Column(nullable = false)
    @ColumnDefault("0")
    private int position = 0;

    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL)
    private List<Document> documents = new ArrayList<>();


    @Builder
    public Folder(Project project, String title, int position, List<Document> documents) {
        this.project = project;
        this.title = title;
        this.position = position;
        this.documents = documents;
    }

    public void updateFolder(FolderDto folderDto) {
        this.title = folderDto.getTitle();
        this.position = folderDto.getPosition();
    }
}

