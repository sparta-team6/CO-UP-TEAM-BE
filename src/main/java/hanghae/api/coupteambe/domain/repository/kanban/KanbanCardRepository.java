package hanghae.api.coupteambe.domain.repository.kanban;

import hanghae.api.coupteambe.domain.entity.kanban.KanbanCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface KanbanCardRepository extends JpaRepository<KanbanCard, UUID> {

}
