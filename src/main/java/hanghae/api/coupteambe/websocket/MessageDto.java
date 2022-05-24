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

    private String senderLoginId;

    private String pjId;

    private String message;

    private LocalDateTime dateTime;
}
