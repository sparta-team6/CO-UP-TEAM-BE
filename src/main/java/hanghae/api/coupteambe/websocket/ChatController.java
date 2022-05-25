package hanghae.api.coupteambe.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final ChattingService chattingService;

    @MessageMapping("/chatting/project")
    public void broadcastProject(MessageDto messageDto) {

        chattingService.saveChatting(messageDto);

        simpMessageSendingOperations.convertAndSend(
                "/sub/chatting/" + messageDto.getPjId(), messageDto);
    }

    @GetMapping("/api/chatting")
    @ResponseBody
    public ResponseEntity<List<MessageDto>> chatLoad(@RequestParam("pjId") String pjId,
            @RequestParam(value = "id", defaultValue = Long.MAX_VALUE + "") Long id) {

        List<MessageDto> messages = chattingService.getChatMessages(pjId, id);

        return ResponseEntity.ok(messages);
    }
}
