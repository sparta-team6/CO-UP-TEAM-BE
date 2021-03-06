package com.hanghae.coupteambe.api.service;

import com.hanghae.coupteambe.api.domain.dto.project.CreateProjectDto;
import com.hanghae.coupteambe.api.domain.dto.project.ReqProjectInfoDto;
import com.hanghae.coupteambe.api.domain.dto.project.ResProjectInfoDto;
import com.hanghae.coupteambe.api.domain.entity.kanban.KanbanBucket;
import com.hanghae.coupteambe.api.domain.entity.member.Member;
import com.hanghae.coupteambe.api.domain.entity.project.Project;
import com.hanghae.coupteambe.api.domain.entity.project.ProjectMember;
import com.hanghae.coupteambe.api.domain.repository.kanban.KanbanBucketRepository;
import com.hanghae.coupteambe.api.domain.repository.member.MemberRepository;
import com.hanghae.coupteambe.api.domain.repository.project.ProjectMemberRepository;
import com.hanghae.coupteambe.api.domain.repository.project.ProjectRepository;
import com.hanghae.coupteambe.api.enumerate.ProjectRole;
import com.hanghae.coupteambe.api.enumerate.StatusFlag;
import com.hanghae.coupteambe.api.util.exception.ErrorCode;
import com.hanghae.coupteambe.api.util.exception.RequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.hanghae.coupteambe.api.util.SecurityUtil.getCurrentUsername;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final MemberRepository memberRepository;
    private final KanbanBucketRepository kanbanBucketRepository;

    /**
     * M5-1 프로젝트 생성
     */
    @Transactional
    public ResProjectInfoDto create(CreateProjectDto createProjectDto) {
        // 1. 프로젝트 생성 사용자 추출
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Member> optionalMember = memberRepository.findByLoginId(loginId);
        Member member = optionalMember
                .orElseThrow(() -> new RequestException(ErrorCode.MEMBER_LOGINID_NOT_FOUND_404));

        // 2. 프로젝트 생성
        Project project = new Project(createProjectDto);

        // 카운팅
        long cntProjects = projectMemberRepository.countAllByMember_Id(member.getId());
        // 3. 프로젝트 연관관계 설정 (관리자 권한 유저)
        ProjectMember projectMember = new ProjectMember(member, project, ProjectRole.ADMIN,((int) cntProjects));

        // 2. 새 버킷 객체를 생성한다.
        KanbanBucket todoBucket = KanbanBucket.builder()
                                              .project(project)
                                              .title("대기")
                                              .position(0)
                                              .build();
        KanbanBucket inProgressBucket = KanbanBucket.builder()
                                                    .project(project)
                                                    .title("진행")
                                                    .position(1)
                                                    .build();
        KanbanBucket doneBucket = KanbanBucket.builder()
                                              .project(project)
                                              .title("완료")
                                              .position(2)
                                              .build();
        List<KanbanBucket> buckets = Arrays.asList(todoBucket, inProgressBucket, doneBucket);

        // 4. 프로젝트 저장
        projectMember = projectMemberRepository.save(projectMember);

        // 3. 새로 생성한 버킷을 Repository 를 이용하여 DB에 저장한다.
        kanbanBucketRepository.saveAll(buckets);

        return new ResProjectInfoDto(projectMember);
    }

    /**
     * M5-2 프로젝트 수정
     */
    @Transactional
    public void modify(UUID pjId, ReqProjectInfoDto reqProjectInfoDto) {

        // 1. 프로젝트 ID 를 key 로 해당 프로젝트 조회
        Optional<Project> optionalProject = projectRepository.findById(pjId);
        // 1-1. 프로젝트가 존재하지 않는 경우, 예외 처리
        Project project = optionalProject
                .orElseThrow(() -> new RequestException(ErrorCode.PROJECT_NOT_FOUND_404));

        // 2. 프로젝트가 존재하는 경우, 프로젝트 정보 수정
        project.updateProject(reqProjectInfoDto);
    }

    /**
     * M5-3 프로젝트 초대코드로 참가
     */
    public String inviteProject(String inviteCode) {

        // 1. 초대 코드를 가진 프로젝트를 조회한다.
        Optional<Project> optionalProject = projectRepository.findByInviteCode(inviteCode);
        // 1-1. 프로젝트가 존재하지 않는 경우 예외처리한다.
        Project project = optionalProject
                .orElseThrow(() -> new RequestException(ErrorCode.PROJECT_NOT_FOUND_404));

        // 1-2. 삭제 처리된 프로젝트에 참가 요청하는 경우, 예외 처리
        if (project.getDelFlag().equals(StatusFlag.DELETED)) {
            throw new RequestException(ErrorCode.PROJECT_NOT_FOUND_404);
        }

        // 2. 프로젝트가 존재하는 경우
        // 2-1. 현재 로그인한 유저의 멤버 ID 로 멤버 정보(객체)를 조회한다.
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Member> optionalMember = memberRepository.findByLoginId(loginId);
        // 2-2. 해당 멤버가 존재하지 않는 경우 예외처리
        Member member = optionalMember
                .orElseThrow(() -> new RequestException(ErrorCode.MEMBER_LOGINID_NOT_FOUND_404));

        // 3. 프로젝트에 참가하려는 멤버가 프로젝트 멤버 테이블에 존재하는지 확인한다.
        Optional<ProjectMember> optionalProjectMember = projectMemberRepository.findByMemberIdAndProjectId(member.getId(), project.getId());

        // 4. 존재하지 않는 경우에만 참가 시키도록 한다.
        if (!optionalProjectMember.isPresent()) {
            // 4-1. 유저수 카운팅
            long cntProjects = projectMemberRepository.countAllByMember_Id(member.getId());
            // 4-2. 프로젝트에 일반유저를 참가시킨다.
            ProjectMember projectMember = new ProjectMember(member, project, ProjectRole.READ_WRITE,((int) cntProjects));
            // 4-3. 프로젝트 저장
            projectMemberRepository.save(projectMember);
            // 4-4. 프로젝트 ID 반환 (초대코드로 입장한 해당 프로젝트 페이지로 바로 이동시키기 위함) -> 프론트 요청 있었음
            return project.getId().toString();
        } else {
            // 5. 프로젝트멤버 테이블에는 존재하지만, 해당 프로젝트에서 나갔거나 추방당한 사람은 재입장 불가
            if (optionalProjectMember.get().getDelFlag().equals(StatusFlag.DELETED)) {
                throw new RequestException(ErrorCode.DENIED_TO_JOIN_PROJECT_409);
            }
            // 6. 프로젝트에 이미 참가한 경우, 예외처리
            throw new RequestException(ErrorCode.PROJECT_MEMBER_DUPLICATION_409);
        }

    }

    /**
     * M5-4 프로젝트 삭제
     * fixme 현재는 프로젝트를 삭제하면 "프로젝트"만 삭제되는 MVP 위주의 로직이지만
     *  해당 프로젝트와 연관되는 버킷 및 카드들도 삭제처리되도록 변경해야 한다.
     */
    @Transactional
    public void delete(UUID pjId) {
        Optional<Project> optionalProject = projectRepository.findById(pjId);
        Project project = optionalProject
                .orElseThrow(() -> new RequestException(ErrorCode.PROJECT_NOT_FOUND_404));

        project.delete();
    }

    /**
     * M5-5 내 프로젝트 조회
     */
    public List<ResProjectInfoDto> getMyProject() {
        // 1. 현재 로그인한 유저의 ID 조회
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        // 2. 해당 멤버가 존재하지 않는 경우 예외처리
        Optional<Member> optionalMember = memberRepository.findByLoginId(loginId);
        Member member = optionalMember
                .orElseThrow(() -> new RequestException(ErrorCode.MEMBER_LOGINID_NOT_FOUND_404));
        // 3. 해당 멤버가 속한 프로젝트 조회 후 반환
        return projectRepository.findProjectsFromMemberByLoginId_DSL(member.getId());
    }

    /**
     * M5-8 선택 프로젝트 조회
     */
    public ResProjectInfoDto getProject(UUID pjId) {
        //todo 리팩토링 때 세 개 쿼리 한 번에 조인해서 가져올 수 있도록 변경할 것
        String loginId = getCurrentUsername().orElseThrow(() -> new RequestException(ErrorCode.COMMON_BAD_REQUEST_400));
        Optional<Member> optionalMember = memberRepository.findByLoginId(loginId);
        Member member = optionalMember
                .orElseThrow(() -> new RequestException(ErrorCode.MEMBER_LOGINID_NOT_FOUND_404));

        // 1. 프로젝트 ID 를 key 로 해당 프로젝트 조회
        Project project = projectRepository.findById(pjId)
                // 1-1. 존재하지 않는 경우 예외 처리
                .orElseThrow(() -> new RequestException(ErrorCode.PROJECT_NOT_FOUND_404));

        Optional<ProjectMember> optionalProjectMember = projectMemberRepository.findByMemberIdAndProjectId(member.getId(), project.getId());

        ResProjectInfoDto resProjectInfoDto = null;

        // 프로젝트에 현재 참여하고 있는 멤버 또는 추방당하지 않은 멤버만 조회 가능
        if (optionalProjectMember.isPresent()) {
            if (optionalProjectMember.get().getDelFlag().equals(StatusFlag.NORMAL)) {
                resProjectInfoDto = ResProjectInfoDto.builder()
                        .pjId(project.getId())
                        .thumbnail(project.getThumbnail())
                        .title(project.getTitle())
                        .summary(project.getSummary())
                        .inviteCode(project.getInviteCode())
                        .projectRole(optionalProjectMember.get().getRole())
                        .createdTime(project.getCreatedTime())
                        .modifiedTime(project.getModifiedTime())
                        .position(optionalProjectMember.get().getPosition())
                        .build();
            } else {
                throw new RequestException(ErrorCode.PROJECT_FORBIDDEN_403);
            }
        } else {
//            // Public Private 처리 할건지 아닌지 정한 다음에 주석 풀지 말지 정합시다.
//            // 퍼블릭 : role -> 읽기전용 read
//            if (isPublic(project)) {
//                resProjectInfoDto = ResProjectInfoDto.builder()
//                        .pjId(project.getId())
//                        .thumbnail(project.getThumbnail())
//                        .title(project.getTitle())
//                        .summary(project.getSummary())
//                        .inviteCode(project.getInviteCode())
//                        .projectRole(ProjectRole.READ)
//                        .build();
//            } else {
//                // private : 접근오류
//                throw new RequestException(ErrorCode.PROJECT_FORBIDDEN_403);
//            }
            // private : 접근오류
            throw new RequestException(ErrorCode.PROJECT_FORBIDDEN_403);
        }
        return resProjectInfoDto;
    }

    /**
     * M5-9 선택 프로젝트 나가기
     */
    @Transactional
    public void exitProject(UUID pjId) {
        // 1. 현재 로그인한 유저의 ID 조회
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        // 2. 해당 멤버가 존재하지 않는 경우 예외처리
        Optional<Member> optionalMember = memberRepository.findByLoginId(loginId);
        Member member = optionalMember
                .orElseThrow(() -> new RequestException(ErrorCode.MEMBER_LOGINID_NOT_FOUND_404));
        // 3. 프로젝트 참여 여부 조회 후 참여하지 않은 경우 예외처리
        Optional<ProjectMember> optionalProjectMember = projectMemberRepository.findByMemberIdAndProjectId(member.getId(), pjId);
        ProjectMember projectMember = optionalProjectMember
                .orElseThrow(() -> new RequestException(ErrorCode.COMMON_BAD_REQUEST_400));
        // 4. 프로젝트 참여 여부 조회 후 참여한 경우 프로젝트 참여 삭제
        projectMember.delete();
    }


    /**
     * M5-10 선택 프로젝트 추방
     */
    @Transactional
    public void kickProject(UUID pjId, String loginId) {
        // 1. 현재 로그인한 유저의 ID 조회
        String adminLoginId = SecurityContextHolder.getContext().getAuthentication().getName();
        // 2. 해당 멤버가 존재하지 않는 경우 예외처리
        Optional<Member> optionalMember = memberRepository.findByLoginId(adminLoginId);
        Member member = optionalMember
                .orElseThrow(() -> new RequestException(ErrorCode.MEMBER_LOGINID_NOT_FOUND_404));
        // 3. 프로젝트 참여 여부 조회 후 참여하지 않은 경우 예외처리
        Optional<ProjectMember> optionalProjectMember = projectMemberRepository.findByMemberIdAndProjectId(member.getId(), pjId);
        ProjectMember projectMember = optionalProjectMember
                .orElseThrow(() -> new RequestException(ErrorCode.COMMON_BAD_REQUEST_400));
        if (projectMember.getRole().equals(ProjectRole.ADMIN)) {
            // 4. 프로젝트 참여 여부 조회 후 참여한 경우 프로젝트 참여 삭제
            Member findKickMember = memberRepository.findByLoginId(loginId)
                    .orElseThrow(() -> new RequestException(ErrorCode.MEMBER_LOGINID_NOT_FOUND_404));
            Optional<ProjectMember> optionalKickMember = projectMemberRepository.findByMemberIdAndProjectId(findKickMember.getId(), pjId);
            ProjectMember kickMember = optionalKickMember
                    .orElseThrow(() -> new RequestException(ErrorCode.COMMON_BAD_REQUEST_400));
            kickMember.delete();
        } else {
            // private : 접근오류
            throw new RequestException(ErrorCode.COMMON_BAD_REQUEST_400);
        }
    }

    /**
     * M5-11 프로젝트 나가기,추방 한사람 복구
     */
    @Transactional
    public void recoveryProject(UUID pjId, String loginId)  {
        // 1. 현재 로그인한 유저의 ID 조회
        String adminLoginId = SecurityContextHolder.getContext().getAuthentication().getName();
        // 2. 해당 멤버가 존재하지 않는 경우 예외처리
        Optional<Member> optionalMember = memberRepository.findByLoginId(adminLoginId);
        Member member = optionalMember
                .orElseThrow(() -> new RequestException(ErrorCode.MEMBER_LOGINID_NOT_FOUND_404));
        // 3. 프로젝트 참여 여부 조회 후 참여하지 않은 경우 예외처리
        Optional<ProjectMember> optionalProjectMember = projectMemberRepository.findByMemberIdAndProjectId(member.getId(), pjId);
        ProjectMember projectMember = optionalProjectMember
                .orElseThrow(() -> new RequestException(ErrorCode.COMMON_BAD_REQUEST_400));
        if (projectMember.getRole().equals(ProjectRole.ADMIN)) {
            // 4. 프로젝트 참여 여부 조회 후 참여한 경우 프로젝트 참여 삭제
            Member findRecoveryMember = memberRepository.findByLoginId(loginId)
                    .orElseThrow(() -> new RequestException(ErrorCode.MEMBER_LOGINID_NOT_FOUND_404));
            Optional<ProjectMember> optionalRecoveryMember = projectMemberRepository.findByMemberIdAndProjectId(findRecoveryMember.getId(), pjId);
            ProjectMember recoveryMember = optionalRecoveryMember
                    .orElseThrow(() -> new RequestException(ErrorCode.COMMON_BAD_REQUEST_400));
            recoveryMember.recovery();
        } else {
            // private : 접근오류
            throw new RequestException(ErrorCode.COMMON_BAD_REQUEST_400);
        }
    }

    private boolean isPublic(Project project) {
        return true;
    }
}
