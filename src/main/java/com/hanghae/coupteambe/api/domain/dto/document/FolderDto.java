package com.hanghae.coupteambe.api.domain.dto.document;

import com.hanghae.coupteambe.api.domain.entity.document.Folder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FolderDto {

    // 프로젝트 ID
    private UUID pjId;

    // 폴더 ID
    private String dfId;

    //폴더이름
    private String title;

    //배치순서
    private int position;

    //생성시간
    private LocalDateTime createdTime;

    //수정시간
    private LocalDateTime modifiedTime;

    private List<DocumentDto> docs;



    public FolderDto(Folder folder) {
        this.dfId = folder.getId().toString();
        this.title = folder.getTitle();
        this.position = folder.getPosition();
        this.createdTime = folder.getCreatedTime();
        this.modifiedTime = folder.getModifiedTime();

    }
}
