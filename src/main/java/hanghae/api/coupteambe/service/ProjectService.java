package hanghae.api.coupteambe.service;

import hanghae.api.coupteambe.domain.dto.project.CreateProjectDto;
import hanghae.api.coupteambe.domain.dto.project.ReqProjectInfoDto;
import hanghae.api.coupteambe.domain.dto.project.ResProjectInfoDto;
import hanghae.api.coupteambe.domain.entity.kanban.KanbanBucket;
import hanghae.api.coupteambe.domain.entity.member.Member;
import hanghae.api.coupteambe.domain.entity.project.Project;
import hanghae.api.coupteambe.domain.entity.project.ProjectMember;
import hanghae.api.coupteambe.domain.repository.kanban.KanbanBucketRepository;
import hanghae.api.coupteambe.domain.repository.member.MemberRepository;
import hanghae.api.coupteambe.domain.repository.project.ProjectMemberRepository;
import hanghae.api.coupteambe.domain.repository.project.ProjectRepository;
import hanghae.api.coupteambe.enumerate.ProjectRole;
import hanghae.api.coupteambe.enumerate.StatusFlag;
import hanghae.api.coupteambe.util.exception.ErrorCode;
import hanghae.api.coupteambe.util.exception.RequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static hanghae.api.coupteambe.util.SecurityUtil.getCurrentUsername;

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
    public void create(CreateProjectDto createProjectDto) {
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
        projectMemberRepository.save(projectMember);

        // 3. 새로 생성한 버킷을 Repository 를 이용하여 DB에 저장한다.
        kanbanBucketRepository.saveAll(buckets);
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
    public void inviteProject(String inviteCode) {

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
        // 카운팅
        long cntProjects = projectMemberRepository.countAllByMember_Id(member.getId());

        // 3. 프로젝트에 참가하려는 멤버가 프로젝트 멤버 테이블에 존재하는지 확인한다.

        // 존재하지 않는 경우에만 참가 시키도록 한다.
        if (!projectMemberRepository.findByMemberIdAndProjectId(member.getId(), project.getId()).isPresent()) {

            // 3-1. 프로젝트에 일반유저를 참가시킨다.
            ProjectMember projectMember = new ProjectMember(member, project, ProjectRole.READ_WRITE,((int) cntProjects));
            // 3-2. 프로젝트 저장
            projectMemberRepository.save(projectMember);
        } else {
            // 3-3. 프로젝트에 이미 참가한 경우, 예외처리
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
        // 내가 참가한 플젝 접근 -> read & write
        if (optionalProjectMember.isPresent()) {
        // 2. 프로젝트가 존재하는 경우, 프로젝트 객체 리턴
        resProjectInfoDto = ResProjectInfoDto.builder()
                .pjId(project.getId())
                .thumbnail(project.getThumbnail())
                .title(project.getTitle())
                .summary(project.getSummary())
                .inviteCode(project.getInviteCode())
                .projectRole(optionalProjectMember.get().getRole())
                .build();

        // 내가 참가하지 않은 플젝 접근 ( 퍼블릭 vs 프라이빗 )
        } else {
            // 퍼블릭 : role -> 읽기전용 read
            if (isPublic(project)) {
                resProjectInfoDto = ResProjectInfoDto.builder()
                        .pjId(project.getId())
                        .thumbnail(project.getThumbnail())
                        .title(project.getTitle())
                        .summary(project.getSummary())
                        .inviteCode(project.getInviteCode())
                        .projectRole(ProjectRole.READ)
                        .build();
            } else {
                // private : 접근오류
                throw new RequestException(ErrorCode.PROJECT_FORBIDDEN_403);
            }
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
    public void kickProject(UUID pjId, UUID memberId) {
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
        if (projectMember.getRole().equals(ProjectRole.ADMIN)) {
            // 4. 프로젝트 참여 여부 조회 후 참여한 경우 프로젝트 참여 삭제
            Optional<ProjectMember> optionalKickMember = projectMemberRepository.findByMemberIdAndProjectId(memberId, pjId);
            ProjectMember kickMember = optionalKickMember
                    .orElseThrow(() -> new RequestException(ErrorCode.COMMON_BAD_REQUEST_400));
            projectMember.delete();
        } else {
            // private : 접근오류
            throw new RequestException(ErrorCode.COMMON_BAD_REQUEST_400);
        }
    }

    private boolean isPublic(Project project) {
        return true;
    }
}
