package hanghae.api.coupteambe.domain.dto.kanban;

import hanghae.api.coupteambe.enumerate.StatusFlag;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Setter
@Getter
public class BucketInfoDto {

    // 버킷 제목
    @Size(max = 20)
    private String title;

    // 개설자
    @Size(max = 50)
    private String creator;

    // 삭제 여부
    private StatusFlag delFlag;

    // 삭제자
    @Size(max = 50)
    private String eliminator;

    // 배치 순서
    private int position;

    // 최종 편집자
    @Size(max = 50)
    private String editor;

}
