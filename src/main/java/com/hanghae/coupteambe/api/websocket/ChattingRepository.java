package com.hanghae.coupteambe.api.websocket;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChattingRepository extends JpaRepository<ChatMessage, Long>, ChattingRepositoryCustom {

    List<ChatMessage> findTopSizeByPjIdAndIdLessThanOrderByIdDesc(String pjId, Long id);
}
