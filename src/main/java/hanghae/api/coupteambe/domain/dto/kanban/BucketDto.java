package hanghae.api.coupteambe.domain.dto.kanban;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Builder
public class BucketDto {

    // 버킷 ID
    private UUID kbbId;

    // 버킷 제목
    private String title;

    private int position;

    // 해당 버킷 내 카드 정보
    private List<CardInfoDto> cards;

    public BucketDto(UUID kbbId, String title, int position, List<CardInfoDto> cards) {
        this.kbbId = kbbId;
        this.title = title;
        this.position = position;
        this.cards = cards;
    }

    public BucketDto(UUID kbbId, String title, int position) {
        this.kbbId = kbbId;
        this.title = title;
        this.position = position;
        this.cards = new ArrayList<>();
    }

    public void addCard(CardInfoDto cardInfoDto) {
        this.cards.add(cardInfoDto);
    }
}
