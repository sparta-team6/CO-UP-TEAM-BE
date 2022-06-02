package com.hanghae.coupteambe.api.controller;

import com.hanghae.coupteambe.api.domain.dto.ResResultDto;
import com.hanghae.coupteambe.api.domain.dto.member.ReqMemberInfoDto;
import com.hanghae.coupteambe.api.domain.dto.member.ResMemberInfoDto;
import com.hanghae.coupteambe.api.service.member.MemberService;
import com.hanghae.coupteambe.api.util.exception.ErrorCode;
import com.hanghae.coupteambe.api.util.exception.RequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * M4-5 회원 정보 수정
     */
    @PutMapping("/update")
    public ResponseEntity<ResResultDto> modifyMember(@RequestBody ReqMemberInfoDto reqMemberInfoDto) {

        memberService.updateMemberInfo(reqMemberInfoDto);

        // 반환값 : 결과 메시지, 상태값(200)
        return ResponseEntity.ok(new ResResultDto("회원 정보 수정 성공"));
    }

    /**
     * M4-7 프로젝트 멤버 전체 조회
     */
    @GetMapping("/projects")
    public ResponseEntity<List<ResMemberInfoDto>> getProjectMembersInfo(@RequestParam("pjId") String pjId) {

        List<ResMemberInfoDto> membersInfo = memberService.getProjectMembers(UUID.fromString(pjId));

        // 반환값 : 프로젝트 멤버들 정보, 상태값(200)
        return ResponseEntity.ok(membersInfo);
    }

    /**
     * M4-8 멤버 검색 (1인)
     */
    @GetMapping("/")
    public ResponseEntity<ResMemberInfoDto> getOneMember(@RequestParam("loginId") String loginId) {

        //선택멤버 상세 조회 서비스 호출
        ResMemberInfoDto resMemberInfoDto = memberService.getOneMemberInfo(loginId);

        // 반환값 : 멤버 정보, 상태값(200)
        return ResponseEntity.ok(resMemberInfoDto);
    }

    /**
     * M4-9 본인 정보 조회 (현재 로그인한 유저의 정보 조회, jwt토큰 값으로 조회)
     */
    @GetMapping("/myInfo")
    public ResponseEntity<ResMemberInfoDto> getMyInfo() {
        // 로그인한 유저의 유효한 jwt 토큰 값으로 정보 조회할 예정이므로 파라매터 안받아옴

        ResMemberInfoDto memberInfoDto = memberService.getMyInfo();

        // 반환값 : 본인 정보, 상태값(200)
        return ResponseEntity.ok(memberInfoDto);
    }

    /**
     * M4-11 회원탈퇴
     */
    @DeleteMapping("/")
    public ResponseEntity<ResResultDto> deleteMember(@RequestParam("loginId") String loginId) {
        String currentLoginId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!currentLoginId.equals(loginId)) {
            throw new RequestException(ErrorCode.MEMBER_LOGINID_NOT_FOUND_404);
        }
        memberService.deleteMember(loginId);

        // 반환값 : 결과 메시지, 상태값(200)
        return ResponseEntity.ok(new ResResultDto("회원 탈퇴 성공"));
    }
}
