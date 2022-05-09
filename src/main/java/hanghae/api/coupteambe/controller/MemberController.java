package hanghae.api.coupteambe.controller;

import hanghae.api.coupteambe.domain.dto.ResResultDto;
import hanghae.api.coupteambe.domain.dto.member.ReqMemberInfoDto;
import hanghae.api.coupteambe.domain.dto.member.ResMemberInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class MemberController {

    /**
     * M4-5 회원 정보 수정
     */
    @PutMapping("/update")
    public ResponseEntity<ResResultDto> modifyMember(@RequestBody ReqMemberInfoDto reqMemberInfoDto) {

        // 반환값 : 결과 메시지, 상태값(200)
        return ResponseEntity.ok(new ResResultDto("회원 정보 수정 성공"));
    }

    /**
     * M4-7 프로젝트 멤버 전체 조회
     */
    @GetMapping("/projects")
    public ResponseEntity<List<ResMemberInfoDto>> getProjectMembersInfo(@RequestParam("pjId") String pjId) {
        List<ResMemberInfoDto> membersInfo = new ArrayList<>();

        // 반환값 : 프로젝트 멤버들 정보, 상태값(200)
        return ResponseEntity.ok(membersInfo);
    }

    /**
     * M4-8 멤버 검색 (1인)
     */
    @GetMapping("/")
    public ResponseEntity<ResMemberInfoDto> getMemberInfo(@RequestParam("loginId") String loginId) {
        ResMemberInfoDto memberInfo = new ResMemberInfoDto();

        // 반환값 : 멤버 정보, 상태값(200)
        return ResponseEntity.ok(memberInfo);
    }

    /**
     * M4-9 본인 정보 조회 (현재 로그인한 유저의 정보 조회, jwt토큰 값으로 조회)
     */
    @GetMapping("/myInfo")
    public ResponseEntity<ResMemberInfoDto> getMyInfo() {
        // 로그인한 유저의 유효한 jwt 토큰 값으로 정보 조회할 예정이므로 파라매터 안받아옴
        ResMemberInfoDto myInfo = new ResMemberInfoDto();

        // 반환값 : 본인 정보, 상태값(200)
        return ResponseEntity.ok(myInfo);
    }
}
