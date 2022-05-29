package com.hanghae.coupteambe.api.domain.dto.document;

import com.hanghae.coupteambe.api.domain.entity.document.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentDto {
    //폴더번호
    private UUID dfId;
    // 프로젝트 ID
    private UUID pjId;
    //문서번호
    private UUID docId;
    //제목
    private String title;
    //본문
    private String contents;
    //배치순서
    private int position;
    //작성자 닉네임
    private String nickname;
    //생성시간
    private LocalDateTime createdTime;
    //수정시간
    private LocalDateTime modifiedTime;

    public DocumentDto(Document document) {
        this.dfId = document.getFolder().getId();
        this.pjId = document.getFolder().getProject().getId();
        this.docId = document.getId();
        this.title = document.getTitle();
        this.contents = document.getContents();
        this.position = document.getPosition();
        this.nickname = document.getManagerNickname();
        this.createdTime = document.getCreatedTime();
        this.modifiedTime = document.getModifiedTime();
    }


}