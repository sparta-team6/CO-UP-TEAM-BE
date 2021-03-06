package com.hanghae.coupteambe.api.websocket;

import com.hanghae.coupteambe.api.domain.entity.baseentity.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Getter
@Entity
@NoArgsConstructor
public class ChatMessage extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String senderLoginId;

    private String nickname;

    private String profileImage;

    @Column(columnDefinition = "LONGTEXT")
    private String message;

    private String pjId;


    public ChatMessage(MessageDto messageDto) {
        this.senderLoginId = messageDto.getSenderLoginId();
        this.nickname = messageDto.getNickname();
        this.profileImage = messageDto.getProfileImage();
        this.pjId = messageDto.getPjId();
        this.message = messageDto.getMessage();
    }
}

