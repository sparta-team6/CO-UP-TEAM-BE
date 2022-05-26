package hanghae.api.coupteambe.domain.repository.document;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hanghae.api.coupteambe.domain.entity.document.Document;
import hanghae.api.coupteambe.domain.entity.document.Folder;
import hanghae.api.coupteambe.domain.entity.document.QDocument;
import hanghae.api.coupteambe.domain.entity.document.QFolder;
import hanghae.api.coupteambe.enumerate.StatusFlag;
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
