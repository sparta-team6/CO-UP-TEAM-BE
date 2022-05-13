package hanghae.api.coupteambe.controller;

import hanghae.api.coupteambe.domain.dto.ResResultDto;
import hanghae.api.coupteambe.domain.dto.project.CreateProjectDto;
import hanghae.api.coupteambe.domain.dto.project.ReqProjectInfoDto;
import hanghae.api.coupteambe.domain.dto.project.ResProjectInfoDto;
import hanghae.api.coupteambe.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    /**
     * M5-1 프로젝트 생성
     */
    @PostMapping("/")
    public ResponseEntity<ResResultDto> postPj(@RequestBody CreateProjectDto createProjectDto) {
        projectService.create(createProjectDto);

        // 반환값 : 결과 메시지, 상태값(201)
        return new ResponseEntity<>(new ResResultDto("프로젝트 생성완료"), HttpStatus.CREATED);
    }

    /**
     * M5-2 프로젝트 수정
     */
    @PatchMapping("/{pjId}")
    public ResponseEntity<ResResultDto> patchPj(@PathVariable String pjId, @RequestBody ReqProjectInfoDto reqProjectInfoDto) {

        projectService.modify(pjId, reqProjectInfoDto);

        // 반환값 : 결과 메시지, 상태값(200)
        return ResponseEntity.ok(new ResResultDto("프로젝트 수정완료"));
    }

    /**
     * M5-3 프로젝트 초대코드로 참가
     */
    @PostMapping("/invite")
    public ResponseEntity<ResResultDto> postInvite(@RequestParam("inviteCode") String inviteCode) {
        // 반환값 : 결과 메시지, 상태값(200)
        return projectService.inviteProject(inviteCode);
    }

    /**
     * M5-4 프로젝트 삭제
     */
    @DeleteMapping("/{pjId}")
    public ResponseEntity<ResResultDto> deletePj(@PathVariable String pjId) {

        projectService.delete(pjId);

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

}
