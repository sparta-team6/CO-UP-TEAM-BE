package hanghae.api.coupteambe.domain.entity.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hanghae.api.coupteambe.domain.dto.project.CreateProjectDto;
import hanghae.api.coupteambe.domain.entity.baseentity.BaseEntity;
import hanghae.api.coupteambe.domain.entity.document.Document;
import hanghae.api.coupteambe.domain.entity.document.Folder;
import hanghae.api.coupteambe.domain.entity.kanban.KanbanBucket;
import hanghae.api.coupteambe.domain.entity.kanban.KanbanCard;
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

    @Column(columnDefinition = "TEXT")
    private String thumbnail;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(nullable = false, length = 20)
    private String summary;

    @Column(nullable = false, unique = true)
    private String inviteCode;

    @JsonIgnore
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Folder> forders = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Document> documents = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<KanbanBucket> kanbanBuckets = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<KanbanCard> kanbanCards = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<ProjectMember> projectMembers = new ArrayList<>();

    public Project(CreateProjectDto createPJrojectDto) {
        this.thumbnail = createPJrojectDto.getThumbnail();
        this.title = createPJrojectDto.getTitle();
        this.summary = createPJrojectDto.getSummary();
        this.inviteCode = UUID.randomUUID().toString();
    }

    /**
     * 객체지향 목적성으로 등록하는 것이며,
     * 프로젝트를 추가하더라도 데이터베이스에 추가되지 않습니다.
     */
    public void addMembers(ProjectMember projectMember) {
        this.projectMembers.add(projectMember);
    }

    /**
     * 객체지향 목적성으로 등록하는 것이며,
     * 프로젝트를 추가하더라도 데이터베이스에 추가되지 않습니다.
     */
    public void removeMembers(ProjectMember projectMember) {
        this.projectMembers.remove(projectMember);
    }
}
