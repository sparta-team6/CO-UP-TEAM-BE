package hanghae.api.coupteambe.domain.dto.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProjectDto {

    // 이미지주소
    private String thumbnail;
    //프로젝트제목
    private String title;
    //프로젝트개요
    private String summary;

}
