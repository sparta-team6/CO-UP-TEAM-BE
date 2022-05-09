package hanghae.api.coupteambe.domain.dto.kanban;

import lombok.Getter;

@Getter
public class CardInfoDto {

    // 소속 버킷 ID
    private String kbbId;

    // 카드 담당자 (카드 배당업무 담당자)
    private String manager;

    // 카드 제목
    private String title;

    // 배치 순서
    private int position;

}
