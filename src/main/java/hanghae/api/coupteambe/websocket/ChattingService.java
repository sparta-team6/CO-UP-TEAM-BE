package hanghae.api.coupteambe.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChattingService {

    private final ChattingRepository chattingRepository;

    @Transactional
    public void saveChattting(MessageDto messageDto) {

        chattingRepository.save(new ChatMessage(messageDto));
    }
}
