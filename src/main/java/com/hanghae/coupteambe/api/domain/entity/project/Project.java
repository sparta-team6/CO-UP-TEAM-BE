package com.hanghae.coupteambe.api.domain.entity.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hanghae.coupteambe.api.domain.dto.project.CreateProjectDto;
import com.hanghae.coupteambe.api.domain.dto.project.ReqProjectInfoDto;
import com.hanghae.coupteambe.api.domain.entity.baseentity.BaseEntity;
import com.hanghae.coupteambe.api.domain.entity.document.Document;
import com.hanghae.coupteambe.api.domain.entity.document.Folder;
import com.hanghae.coupteambe.api.domain.entity.kanban.KanbanBucket;
import com.hanghae.coupteambe.api.domain.entity.kanban.KanbanCard;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Getter
@NoArgsConstructor
public class Project extends BaseEntity {

    @Column(columnDefinition = "LONGTEXT")
    private String thumbnail;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false)
    private String summary;

    @Column(nullable = false, unique = true)
    private String inviteCode;

    @JsonIgnore
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private final List<Folder> folders = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private final List<Document> documents = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private final List<KanbanBucket> kanbanBuckets = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private final List<KanbanCard> kanbanCards = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private final List<ProjectMember> projectMembers = new ArrayList<>();

    public Project(CreateProjectDto createProjectDto) {
        this.thumbnail = createProjectDto.getThumbnail();
        this.title = createProjectDto.getTitle();
        this.summary = createProjectDto.getSummary();
        this.inviteCode = UUID.randomUUID().toString();
    }

    public void updateProject(ReqProjectInfoDto reqProjectInfoDto) {
        this.thumbnail = reqProjectInfoDto.getThumbnail();
        this.title = reqProjectInfoDto.getTitle();
        this.summary = reqProjectInfoDto.getSummary();
    }

    /**
     * ???????????? ??????????????? ???????????? ?????????,
     * ??????????????? ?????????????????? ????????????????????? ???????????? ????????????.
     */
    public void addMembers(ProjectMember projectMember) {
        this.projectMembers.add(projectMember);
    }

    /**
     * ???????????? ??????????????? ???????????? ?????????,
     * ??????????????? ?????????????????? ????????????????????? ???????????? ????????????.
     */
    public void removeMembers(ProjectMember projectMember) {
        this.projectMembers.remove(projectMember);
    }
}
