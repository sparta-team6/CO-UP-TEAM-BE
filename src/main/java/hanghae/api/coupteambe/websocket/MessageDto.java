package hanghae.api.coupteambe.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDto {

    private Long id;

    private String senderLoginId;

    private String pjId;

    private String message;

    private LocalDateTime dateTime;

    public MessageDto(ChatMessage chatMessage) {
        this.id = chatMessage.getId();
        this.senderLoginId = chatMessage.getSenderLoginId();
        this.pjId = chatMessage.getPjId();
        this.message = chatMessage.getMessage();
        this.dateTime = chatMessage.getCreatedTime();
    }
}
