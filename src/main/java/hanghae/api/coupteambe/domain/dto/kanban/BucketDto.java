package hanghae.api.coupteambe.domain.dto.kanban;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BucketDto {

    // 버킷 ID
    private UUID kbbId;

    // 버킷 제목
    private String title;

    private int position;

    // 해당 버킷 내 카드 정보
    private List<CardInfoDto> cards;

}
