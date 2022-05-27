package com.hanghae.coupteambe.api.domain.repository.document;

import com.hanghae.coupteambe.api.domain.entity.document.Document;
import com.hanghae.coupteambe.api.domain.entity.document.Folder;
import com.hanghae.coupteambe.api.domain.entity.document.QDocument;
import com.hanghae.coupteambe.api.domain.entity.document.QFolder;
import com.hanghae.coupteambe.api.enumerate.StatusFlag;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class DocumentFolderRepositoryImpl implements DocumentFolderRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Folder> findFoldersAndDocumentsByProject_Id_DSL(String projectId) {
        QFolder folder = QFolder.folder;

        return jpaQueryFactory.select(folder)
                .from(folder)
                .where(folder.project.id.eq(UUID.fromString(projectId))
                        .and(folder.delFlag.eq(StatusFlag.NORMAL)))
                .orderBy(folder.createdTime.asc())
                .distinct().fetch();
    }

    @Override
    public List<Document> findDocumentByFolder_Id_DSL(UUID folderId) {
        QDocument document = QDocument.document;

        return jpaQueryFactory.select(document)
                .from(document)
                .where(document.folder.id.eq(folderId)
                        .and(document.delFlag.eq(StatusFlag.NORMAL)))
                .orderBy(document.createdTime.asc())
                .distinct().fetch();
    }
}
