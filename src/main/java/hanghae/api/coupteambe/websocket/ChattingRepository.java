package hanghae.api.coupteambe.websocket;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChattingRepository extends JpaRepository<ChatMessage, UUID> {

}
