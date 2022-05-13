package hanghae.api.coupteambe.service;

import hanghae.api.coupteambe.domain.dto.project.CreateProjectDto;
import hanghae.api.coupteambe.domain.dto.project.ReqProjectInfoDto;
import hanghae.api.coupteambe.domain.dto.project.ResProjectInfoDto;
import hanghae.api.coupteambe.domain.entity.member.Member;
import hanghae.api.coupteambe.domain.entity.project.Project;
import hanghae.api.coupteambe.domain.entity.project.ProjectMember;
import hanghae.api.coupteambe.domain.repository.member.MemberRepository;
import hanghae.api.coupteambe.domain.repository.project.ProjectMemberRepository;
import hanghae.api.coupteambe.domain.repository.project.ProjectRepository;
import hanghae.api.coupteambe.domain.repository.project.ProjectRepositoryImpl;
import hanghae.api.coupteambe.util.exception.ErrorCode;
import hanghae.api.coupteambe.util.exception.RequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final MemberRepository memberRepository;
    private final ProjectRepositoryImpl projectRepositoryImpl;

    /**
     * M5-1 프로젝트 생성
     */
    @Transactional
    public void create(CreateProjectDto createProjectDto) {
        // 1. 프로젝트 생성 사용자 추출
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Member> optionalMember = memberRepository.findByLoginId(loginId);
        Member member = optionalMember.orElseThrow(() -> new RequestException(ErrorCode.MEMBER_LOGINID_NOT_FOUND_404));

        // 2. 프로젝트 생성
        Project project = new Project(createProjectDto);

        // 3. 프로젝트 연관관계 설정
        ProjectMember projectMember = new ProjectMember(member, project);

        // 4. 프로젝트 저장
        projectMemberRepository.save(projectMember);
    }

    /**
     * M5-2 프로젝트 수정
     */
    @Transactional
    public void modify(String pjId, ReqProjectInfoDto reqProjectInfoDto) {

        // 1. 프로젝트 ID 를 key 로 해당 프로젝트 조회
        Optional<Project> optionalProject = projectRepository.findById(UUID.fromString(pjId));
        // 1-1. 프로젝트가 존재하지 않는 경우, 예외 처리
        Project project = optionalProject.orElseThrow(() -> new RequestException(ErrorCode.PROJECT_NOT_FOUND_404));

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
        Project project = optionalProject.orElseThrow(() -> new RequestException(ErrorCode.PROJECT_NOT_FOUND_404));

        // 2. 프로젝트가 존재하는 경우
        // 2-1. 현재 로그인한 유저의 멤버 ID 로 멤버 정보(객체)를 조회한다.
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Member> optionalMember = memberRepository.findByLoginId(loginId);
        // 2-2. 해당 멤버가 존재하지 않는 경우 예외처리
        Member member = optionalMember.orElseThrow(() -> new RequestException(ErrorCode.MEMBER_LOGINID_NOT_FOUND_404));

        // 3. 프로젝트에 멤버를 추가한다.
        ProjectMember projectMember = new ProjectMember(member, project);

        // 4. 프로젝트 저장
        projectMemberRepository.save(projectMember);
    }

    /**
     * M5-4 프로젝트 삭제
     * fixme 현재는 프로젝트를 삭제하면 "프로젝트"만 삭제되는 MVP 위주의 로직이지만
     *  해당 프로젝트와 연관되는 버킷 및 카드들도 삭제처리되도록 변경해야 한다.
     */
    @Transactional
    public void delete(String pjId) {
        // 파라매터로 받은 프로젝트 ID를 key 로 DB 에서 해당 프로젝트를 삭제한다.
        projectRepository.deleteById(UUID.fromString(pjId));
    }

    /**
     * M5-5 내 프로젝트 조회
     */
    public List<ResProjectInfoDto> getMyProject() {
        // 현재 로그인한 유저의 ID 조회
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();

        // 유저 ID 를 가지고 있는 프로젝트 리스트 조회 후 반환
        return projectRepositoryImpl.findProjectsFromMemberByLoginId_DSL(loginId);
    }
}
