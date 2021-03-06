package com.hanghae.coupteambe.api.controller;

import com.hanghae.coupteambe.api.domain.dto.ResResultDto;
import com.hanghae.coupteambe.api.domain.dto.project.CreateProjectDto;
import com.hanghae.coupteambe.api.domain.dto.project.ReqProjectInfoDto;
import com.hanghae.coupteambe.api.domain.dto.project.ResProjectInfoDto;
import com.hanghae.coupteambe.api.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    /**
     * M5-1 프로젝트 생성
     */
    @PostMapping("/")
    public ResponseEntity<ResProjectInfoDto> postPj(@RequestBody CreateProjectDto createProjectDto) {

        ResProjectInfoDto resProjectInfoDto = projectService.create(createProjectDto);

        // 반환값 : 결과 메시지, 상태값(201)
        return ResponseEntity.ok(resProjectInfoDto);
        }

    /**
     * M5-2 프로젝트 수정
     */
    @PatchMapping("/{pjId}")
    public ResponseEntity<ResResultDto> patchPj(@PathVariable String pjId, @RequestBody ReqProjectInfoDto reqProjectInfoDto) {

        projectService.modify(UUID.fromString(pjId), reqProjectInfoDto);

        // 반환값 : 결과 메시지, 상태값(200)
        return ResponseEntity.ok(new ResResultDto("프로젝트 수정완료"));
    }

    /**
     * M5-3 프로젝트 초대코드로 참가
     */
    @PostMapping("/invite")
    public ResponseEntity<String> postInvite(@RequestParam("inviteCode") String inviteCode) {
        // 반환값 : 결과 메시지, 상태값(200)
        return ResponseEntity.ok(projectService.inviteProject(inviteCode));
    }

    /**
     * M5-4 프로젝트 삭제
     */
    @DeleteMapping("/{pjId}")
    public ResponseEntity<ResResultDto> deletePj(@PathVariable String pjId) {

        projectService.delete(UUID.fromString(pjId));

        // 반환값 : 결과 메시지, 상태값(200)
        return ResponseEntity.ok(new ResResultDto("프로젝트 삭제완료"));
    }

    /**
     * M5-5 내 프로젝트들 조회
     */
    @GetMapping("/")
    public ResponseEntity<List<ResProjectInfoDto>> getProjects() {

        List<ResProjectInfoDto> resProjectInfoDtoList = projectService.getMyProject();

        // 반환값 : 결과 메시지, 상태값(200)
        return ResponseEntity.ok(resProjectInfoDtoList);
    }

    /**
     * M5-8 선택 프로젝트 조회
     */
    @GetMapping("/{pjId}")
    public ResponseEntity<ResProjectInfoDto> getProject(@PathVariable String pjId) {

        ResProjectInfoDto projectInfoDto = projectService.getProject(UUID.fromString(pjId));

        return ResponseEntity.ok(projectInfoDto);
    }

    /**
     * M5-9 선택 프로젝트 나가기
     */
    @DeleteMapping("/exit/{pjId}")
    public ResponseEntity<ResResultDto> deleteProject(@PathVariable String pjId) {

        projectService.exitProject(UUID.fromString(pjId));

        return ResponseEntity.ok(new ResResultDto("프로젝트 나가기 완료"));

    }

    /**
     * M5-10 선택 프로젝트 추방
     */
    @DeleteMapping("/kick/{pjId}&{loginId}")
    public ResponseEntity<ResResultDto> kickProject(@PathVariable String pjId, @PathVariable String loginId) {

        projectService.kickProject(UUID.fromString(pjId),(loginId));

        return ResponseEntity.ok(new ResResultDto("프로젝트 추방 완료"));


    }

    /**
     * M5-11 프로젝트 나가기,추방 한사람 복구
     */
    @PatchMapping("/recovery/{pjId}&{loginId}")
    public ResponseEntity<ResResultDto> recoveryProject(@PathVariable String pjId, @PathVariable String loginId) {

        projectService.recoveryProject(UUID.fromString(pjId),(loginId));

        return ResponseEntity.ok(new ResResultDto("회원복구 완료"));


    }
}
