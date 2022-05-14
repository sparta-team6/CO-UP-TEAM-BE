package hanghae.api.coupteambe.domain.repository.document;

import hanghae.api.coupteambe.domain.entity.document.Folder;

import java.util.List;

public interface DocumentFolderRepositoryCustom {

    //XXX QueryDSL 을 이용한 메소드는 '_DSL' 을 접미사로 붙여주세요.
    // 예, [void updateEntityById_DSL]

    List<Folder> findFoldersAndDocumentsByProject_Id_DSL(String projectId);
}
