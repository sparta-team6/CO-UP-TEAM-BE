package hanghae.api.coupteambe.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final ChattingService chattingService;

    @MessageMapping("/chatting/project")
    public void broadcastProject(MessageDto messageDto) {

        chattingService.saveChattting(messageDto);

        simpMessageSendingOperations.convertAndSend(
                "/sub/chatting/" + messageDto.getPjId(), messageDto);
    }
}
