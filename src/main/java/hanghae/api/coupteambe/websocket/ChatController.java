package hanghae.api.coupteambe.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @MessageMapping("/chatting")
    public void message(Message message) {
        simpMessageSendingOperations.convertAndSend("/sub/chatting/project/" + message.getProjectId(), message);
    }
}
