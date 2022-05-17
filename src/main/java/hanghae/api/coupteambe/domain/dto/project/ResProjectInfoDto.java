package hanghae.api.coupteambe.domain.dto.project;

import hanghae.api.coupteambe.enumerate.ProjectRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResProjectInfoDto {

    //프로젝트번호
    private UUID pjId;
    // 이미지주소
    private String thumbnail;
    //프로젝트제목
    private String title;
    //프로젝트개요
    private String summary;
    //초대코드
    private String inviteCode;
    // 현재 사용자 권한
    private ProjectRole projectRole;

    public void updateRole(ProjectRole projectRole) {
        this.projectRole = projectRole;
    }
}