package hanghae.api.coupteambe.domain.repository.notice;

import hanghae.api.coupteambe.domain.entity.notice.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NoticeRepository extends JpaRepository<Notice, UUID> {
}
