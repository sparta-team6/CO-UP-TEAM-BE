package hanghae.api.coupteambe.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChattingService {

    private final ChattingRepository chattingRepository;

    @Transactional
    public void saveChatting(MessageDto messageDto) {

        chattingRepository.save(new ChatMessage(messageDto));
    }

    public List<MessageDto> getChatMessages(String pjId, Long id) {
        List<ChatMessage> chatMessages = chattingRepository.findTop20ByPjIdAndIdLessThanOrderByIdDesc(
                pjId, id);
        return chatMessages.stream().map(MessageDto::new).collect(Collectors.toList());
    }
}
