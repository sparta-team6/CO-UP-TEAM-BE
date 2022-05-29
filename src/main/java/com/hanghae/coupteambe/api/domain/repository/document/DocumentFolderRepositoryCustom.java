package com.hanghae.coupteambe.api.domain.repository.document;

import com.hanghae.coupteambe.api.domain.dto.document.DocumentDto;
import com.hanghae.coupteambe.api.domain.entity.document.Document;
import com.hanghae.coupteambe.api.domain.entity.document.Folder;

import java.util.List;
import java.util.UUID;

public interface DocumentFolderRepositoryCustom {

    //XXX QueryDSL 을 이용한 메소드는 '_DSL' 을 접미사로 붙여주세요.
    // 예, [void updateEntityById_DSL]

    List<Folder> findFoldersAndDocumentsByProject_Id_DSL(String projectId);
    List<Document> findDocumentByFolder_Id_DSL(UUID folderId);
    DocumentDto findLastestDocument_DSL(String projectId);
}
