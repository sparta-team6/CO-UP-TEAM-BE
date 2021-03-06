package com.hanghae.coupteambe.api.domain.entity.project;

import com.hanghae.coupteambe.api.domain.entity.baseentity.BaseTimeEntity;
import com.hanghae.coupteambe.api.domain.entity.member.Member;
import com.hanghae.coupteambe.api.enumerate.ProjectRole;
import com.hanghae.coupteambe.api.enumerate.StatusFlag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class ProjectMember extends BaseTimeEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Type(type = "org.hibernate.type.UUIDCharType")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "pjId")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mbId")
    private Member member;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int position = 0;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProjectRole role;

    @Enumerated(EnumType.STRING)
    private StatusFlag delFlag = StatusFlag.NORMAL;

    public void delete() {
        this.delFlag = StatusFlag.DELETED;
    }
    public void recovery() {
        this.delFlag = StatusFlag.NORMAL;
    }

    public ProjectMember(Member member, Project project, ProjectRole projectRole, int position) {
        this.member = member;
        member.addProjects(this);

        this.project = project;
        project.addMembers(this);

        this.role = projectRole;

        this.position = position;
    }
}
