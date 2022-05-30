package com.hanghae.coupteambe.api.websocket;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.hanghae.coupteambe.api.websocket.QChatMessage.chatMessage;

@RequiredArgsConstructor
public class ChattingRepositoryImpl implements ChattingRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ChatMessage> getChattingMessages(String pjId, Long id, Integer size) {
        return jpaQueryFactory.selectFrom(chatMessage)
                              .where(chatMessage.pjId.eq(pjId).and(chatMessage.id.lt(id)))
                              .orderBy(chatMessage.id.desc())
                              .limit(size)
                              .fetch();
    }
}
