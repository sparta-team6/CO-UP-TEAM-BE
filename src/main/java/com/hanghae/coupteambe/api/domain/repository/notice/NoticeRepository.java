package com.hanghae.coupteambe.api.domain.repository.notice;

import com.hanghae.coupteambe.api.domain.entity.notice.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NoticeRepository extends JpaRepository<Notice, UUID>, NoticeRepositoryCustom {
}
