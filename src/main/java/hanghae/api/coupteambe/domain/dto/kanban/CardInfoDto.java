package hanghae.api.coupteambe.domain.dto.kanban;

import hanghae.api.coupteambe.enumerate.StatusFlag;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Setter
@Getter
public class CardInfoDto {

    // 소속 버킷 ID
    private String kbbId;

    // 카드 담당자 (카드 배당업무 담당자)
    private String manager;

    // 카드 제목
    @Size(max = 20)
    private String title;

    // 카드 본문
    private String contents;

    // 카드 개설자 (해당 카드 최초 생성자)
    @Size(max = 50)
    private String creator;

    // 삭제 여부 (기본값 = 정상)
    private StatusFlag delFlag;

    // 카드 삭제자
    @Size(max = 50)
    private String eliminator;

    // 배치 순서
    private int position;

    // 최종 편집자
    @Size(max = 50)
    private String editor;

}
