package hanghae.api.coupteambe.domain.dto.kanban;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.List;

@Setter
@Getter
public class BucketDto {

    // 버킷 제목
    @Size(max = 20)
    private String title;

    // 해당 버킷 내 카드 정보
    private List<CardInfoDto> cards;

}
