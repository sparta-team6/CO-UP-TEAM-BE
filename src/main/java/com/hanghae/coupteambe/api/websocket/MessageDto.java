package com.hanghae.coupteambe.api.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDto {

    private Long id;

    private String senderLoginId;

    private String nickname;

    private String profileImage;

    private String pjId;

    private String message;

    private LocalDateTime dateTime;

    public MessageDto(ChatMessage chatMessage) {
        this.id = chatMessage.getId();
        this.senderLoginId = chatMessage.getSenderLoginId();
        this.nickname = chatMessage.getNickname();
        this.profileImage = chatMessage.getProfileImage();
        this.pjId = chatMessage.getPjId();
        this.message = chatMessage.getMessage();
        this.dateTime = chatMessage.getCreatedTime();
    }
}
