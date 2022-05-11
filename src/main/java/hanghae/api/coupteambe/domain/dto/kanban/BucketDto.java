package hanghae.api.coupteambe.domain.dto.kanban;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BucketDto {

    // 버킷 ID
    private String kbbId;

    // 버킷 제목
    private String title;

    // 해당 버킷 내 카드 정보
    private List<CardInfoDto> cards;

}
