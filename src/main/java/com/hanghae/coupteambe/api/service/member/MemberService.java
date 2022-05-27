package com.hanghae.coupteambe.api.service.member;

import com.hanghae.coupteambe.api.domain.dto.member.ReqMemberInfoDto;
import com.hanghae.coupteambe.api.domain.dto.member.ResMemberInfoDto;
import com.hanghae.coupteambe.api.domain.entity.member.Member;
import com.hanghae.coupteambe.api.domain.entity.project.Project;
import com.hanghae.coupteambe.api.domain.repository.member.MemberRepository;
import com.hanghae.coupteambe.api.domain.repository.member.MemberRepositoryImpl;
import com.hanghae.coupteambe.api.domain.repository.project.ProjectRepository;
import com.hanghae.coupteambe.api.util.exception.ErrorCode;
import com.hanghae.coupteambe.api.util.exception.RequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepositoryImpl memberRepositoryImpl;

    @Transactional
    ResMemberInfoDto getMemberInfo(String loginId) {

        Optional<Member> optionalMember = memberRepository.findByLoginId(loginId);

        Member member = optionalMember.orElseThrow(() -> new RequestException(ErrorCode.MEMBER_LOGINID_NOT_FOUND_404));
        return new ResMemberInfoDto(member);
    }

    /**
     * M4-5. 회원 정보 수정
     */
    @Transactional
    public void updateMemberInfo(ReqMemberInfoDto reqMemberInfoDto) {

        // 1. Request 에서 유저 로그인 ID(Email) 추출
        String loginId = reqMemberInfoDto.getLoginId();

        // 2. 로그인 ID 로 DB 에서 유저 조회
        Optional<Member> optionalMember = memberRepository.findByLoginId(loginId);
        // 2-1. DB 에서 조회되지 않는 경우, 404에러 반환
        Member member = optionalMember.orElseThrow(() -> new RequestException(ErrorCode.MEMBER_LOGINID_NOT_FOUND_404));

        // 3. 조회에 성공했을 시, 유저 정보 변경
        member.updateMember(reqMemberInfoDto);
    }

    /**
     * M4-7. 프로젝트 멤버 전체 조회
     */
    public List<ResMemberInfoDto> getProjectMembers(UUID pjId) {
        // 1. 파라매터로 받은 프로젝트 ID 를 Key 로 해당 프로젝트가 존재하는지 조회
        Optional<Project> optionalProject = projectRepository.findById(pjId);
        // 1-1. 프로젝트가 존재하지 않는 경우, 예외 처리
        optionalProject.orElseThrow(() -> new RequestException(ErrorCode.PROJECT_NOT_FOUND_404));

        // 2. 프로젝트가 존재하는 경우, 프로젝트 ID 를 Key 로 프로젝트에 참가한 전체 멤버 조회
        return memberRepositoryImpl.findMembersFromProjectByProjectId_DSL(pjId);
    }

    /**
     * M4-8 멤버 검색(1인)
     */
    @Transactional
    public ResMemberInfoDto getOneMemberInfo(String loginId) {

        // 1. 파라미터로 받은 loginId를 key로 멤버 정보를 조회한다
        Optional<Member> optionalMember = memberRepository.findByLoginId(loginId);
        Member member = optionalMember.orElseThrow(
                () -> new RequestException(ErrorCode.MEMBER_LOGINID_NOT_FOUND_404));

        // 2. 새 멤버 객체를 생성하고 조회한 멤버로 초기화한다.
        // 3. 생성된 멤버 객체를 반환한다.

        return new ResMemberInfoDto(member);
    }


    /**
     * M4-9 본인 정보 조회
     */
    public ResMemberInfoDto getMyInfo() {
        // 1. 현재 로그인한 유저의 ID 조회
        String LoginId = SecurityContextHolder.getContext().getAuthentication().getName();
        // 2. 현재 멤버가 존재하지 않는 경우 예외처리
        Optional<Member> optionalMember = memberRepository.findByLoginId(LoginId);
        Member member = optionalMember.orElseThrow(() -> new RequestException(ErrorCode.MEMBER_LOGINID_NOT_FOUND_404));

        // 3. 해당 멤버 조회 후 반환
        return new ResMemberInfoDto(member);
    }


}
