package hanghae.api.coupteambe.domain.repository.document;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hanghae.api.coupteambe.domain.entity.document.Folder;
import hanghae.api.coupteambe.domain.entity.document.QDocument;
import hanghae.api.coupteambe.domain.entity.document.QFolder;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class DocumentFolderRepositoryImpl implements DocumentFolderRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Folder> findFoldersAndDocumentsByProject_Id_DSL(String projectId) {
        QFolder folder = QFolder.folder;
        QDocument document = QDocument.document;

        return jpaQueryFactory.select(folder)
                .from(folder)
                .innerJoin(folder.documents, document)
                .fetchJoin()
                .where(folder.project.id.eq(UUID.fromString(projectId)))
                .distinct().fetch();


    }
}
