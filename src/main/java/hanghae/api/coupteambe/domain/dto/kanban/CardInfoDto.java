package hanghae.api.coupteambe.domain.dto.kanban;

import hanghae.api.coupteambe.domain.entity.kanban.KanbanCard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardInfoDto {

    // 카드 ID
    private String kbcId;

    // 버킷 ID
    private String kbbId;

    // 카드 담당자 (카드 배당업무 담당자)
    private String manager;

    // 카드 제목
    private String title;

    // 카드 내용
    private String contents;

    // 배치 순서
    private int position;

    public CardInfoDto(KanbanCard kanbanCard) {

        this.kbcId = kanbanCard.getId().toString();
        this.manager = kanbanCard.getManager();
        this.title = kanbanCard.getTitle();
        this.contents = kanbanCard.getContents();
        this.position = kanbanCard.getPosition();
    }

}
