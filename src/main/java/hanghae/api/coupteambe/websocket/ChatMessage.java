package hanghae.api.coupteambe.websocket;

import hanghae.api.coupteambe.domain.entity.baseentity.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Getter
@Entity
@NoArgsConstructor
public class ChatMessage extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String senderLoginId;

    @Column(columnDefinition = "TEXT")
    private String message;

    private String pjId;


    public ChatMessage(MessageDto messageDto) {
        this.senderLoginId = messageDto.getSenderLoginId();
        this.pjId = messageDto.getPjId();
        this.message = messageDto.getMessage();
    }
}

