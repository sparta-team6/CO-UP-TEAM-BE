package com.hanghae.coupteambe.api.service;

import com.hanghae.coupteambe.api.domain.dto.document.DocumentDto;
import com.hanghae.coupteambe.api.domain.dto.document.FolderDto;
import com.hanghae.coupteambe.api.domain.entity.document.Document;
import com.hanghae.coupteambe.api.domain.entity.document.Folder;
import com.hanghae.coupteambe.api.domain.entity.project.Project;
import com.hanghae.coupteambe.api.domain.repository.document.DocumentFolderRepository;
import com.hanghae.coupteambe.api.domain.repository.project.ProjectRepository;
import com.hanghae.coupteambe.api.util.exception.ErrorCode;
import com.hanghae.coupteambe.api.util.exception.RequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.lang.model.type.ArrayType;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final DocumentFolderRepository documentFolderRepository;
    private final ProjectRepository projectRepository;


    /**
     * M1-1 폴더 생성
     */
    @Transactional
    public FolderDto createFolders(FolderDto folderDto) {

        // 1. 파라미터로 받은 폴더 객체에서 필요한 데이터를 추출한다.
        UUID projectId = folderDto.getPjId();
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        Project project = optionalProject.orElseThrow(() -> new RequestException(ErrorCode.PROJECT_NOT_FOUND_404));

        // 카운팅
        long cntFolders = documentFolderRepository.countAllByproject_Id(projectId);

        // 2. 새 폴더 객체를 생성한다.
        Folder newfolder = Folder.builder()
                                 .project(project)
                                 .title(folderDto.getTitle())
                                 .position((int) cntFolders)
                                 .build();

        // 3. 새로 생성한 폴더를 Repository 를 이용하여 DB에 저장한다.
        return new FolderDto(documentFolderRepository.save(newfolder));
    }

    /**
     * M1-2 폴더 수정
     */
    @Transactional
    public void modifyFolders(FolderDto folderDto) {

        // 1. 파라매터로 받은 폴더 객체에서 필요한 데이터를 추출한다.
        String folderId = folderDto.getDfId();

        // 2. 폴더 ID를 key 로 해당 폴더를 DB에서 조회한다 Repository(JPA)이용
        Optional<Folder> optionalFolder = documentFolderRepository.findById(UUID.fromString(folderId));
        Folder folder = optionalFolder.orElseThrow(
                () -> new RequestException(ErrorCode.FOLDER_NOT_FOUND_404));

        // 3. 새 폴더 객체를 생성하고 조회한 버킷으로 초기화 한다.
        // 4. 폴더를 업데이트 시킨다.
        folder.updateFolder(folderDto);
    }

    /**
     * M1-3 폴더 삭제
     */
    @Transactional
    public void deleteFolders(String dfId) {

        Optional<Folder> optionalFolder = documentFolderRepository.findById(UUID.fromString(dfId));
        Folder folder = optionalFolder.orElseThrow(
                () -> new RequestException(ErrorCode.FOLDER_NOT_FOUND_404));
        List<Document> documents = folder.getDocuments();
        documents.forEach(document -> document.delete());
        folder.delete();
    }


    /**
     * M1-4 전체 폴더, 문서 조회
     */
    @Transactional
    public List<FolderDto> getAllFoldersAndDocuments(String projectId) {

        // 1. 파라미터로 받은 프로젝트 ID를 가지고 있는 모든 폴더를 조회한다.

        // 2. 순차적으로 조회된 폴더의 폴더 ID를 가지고있는 모든 문서들을 조회한다.
        List<Folder> folders = documentFolderRepository.findFoldersAndDocumentsByProject_Id_DSL(projectId);
        List<FolderDto> folderDtos = new ArrayList<>();
        folders.forEach(folder -> {
            List<DocumentDto> documentDtos = new ArrayList<>();
            List<Document> documents = documentFolderRepository.findDocumentByFolder_Id_DSL(folder.getId());

            documents.forEach(document -> {
                documentDtos.add(new DocumentDto(document));
            });
            folderDtos.add(FolderDto.builder()
                    .pjId(folder.getProject().getId())
                    .dfId(folder.getId().toString())
                    .title(folder.getTitle())
                    .position(folder.getPosition())
                    .createdTime(folder.getCreatedTime())
                    .modifiedTime(folder.getModifiedTime())
                    .docs(documentDtos)
                    .build());

/// TODO: 2022/05/12 테스트해보자
        });
        return folderDtos;
    }
}