package com.hanghae.coupteambe.api.websocket;

import java.util.List;

public interface ChattingRepositoryCustom {

    List<ChatMessage> getChattingMessages(String pjId, Long id, Integer size);
}
