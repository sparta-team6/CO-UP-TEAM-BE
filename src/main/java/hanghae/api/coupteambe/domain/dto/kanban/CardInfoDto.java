package hanghae.api.coupteambe.domain.dto.kanban;

import hanghae.api.coupteambe.domain.entity.kanban.KanbanCard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardInfoDto {

    // 카드 ID
    private UUID kbcId;

    // 버킷 ID
    private UUID kbbId;

    // 카드 담당자 (카드 배당업무 담당자)
    private String manager;

    private String managerNickname;

    // 카드 제목
    private String title;

    // 카드 내용
    private String contents;

    // 배치 순서
    private int position;

    public CardInfoDto(KanbanCard kanbanCard) {

        this.kbcId = kanbanCard.getId();
        this.kbbId = kanbanCard.getKanbanBucket().getId();
        this.manager = kanbanCard.getManager();
        this.managerNickname = kanbanCard.getManagerNickname();
        this.title = kanbanCard.getTitle();
        this.contents = kanbanCard.getContents();
        this.position = kanbanCard.getPosition();
    }

    public CardInfoDto(String title, String contents, int position) {
        this.title = title;
        this.contents = contents;
        this.position = position;
    }

}
