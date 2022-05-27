package com.hanghae.coupteambe.api.domain.entity.document;

import com.hanghae.coupteambe.api.domain.dto.document.FolderDto;
import com.hanghae.coupteambe.api.domain.entity.baseentity.BaseEntity;
import com.hanghae.coupteambe.api.domain.entity.project.Project;
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
    private int position;

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
    }
}

