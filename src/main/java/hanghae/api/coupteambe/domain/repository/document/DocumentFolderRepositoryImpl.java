package hanghae.api.coupteambe.domain.repository.document;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hanghae.api.coupteambe.domain.entity.document.Folder;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class DocumentFolderRepositoryImpl implements DocumentFolderRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Folder> findFoldersAndDocumentsByProject_Id(String projectId) {
        return null;
    }
}
