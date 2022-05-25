package hanghae.api.coupteambe.websocket;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChattingRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findTop20ByPjIdAndIdLessThanOrderByIdDesc(String pjId, Long id);
}
