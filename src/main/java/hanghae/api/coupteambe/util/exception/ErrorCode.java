package hanghae.api.coupteambe.util.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 참고사이트
 * <a href="https://github.com/HangHae99ProjectTeam10/SharePod-Server/blob/main/src/main/java/com/spring/sharepod/exception/CommonError/ErrorCode.java">https://github.com/HangHae99ProjectTeam10/SharePod-Server/blob/main/src/main/java/com/spring/sharepod/exception/CommonError/ErrorCode.java</a>
 * <br>
 * <pre>
 * 4xx 클라이언트 오류
 * 400 Bad Request : 잘못된 요청.
 * 401 Unauthorized : 인증이 필요함.
 * 403 Forbidden : 접근 권한 없음.
 * 404 Not Found : Resource 없음.
 * 405 Methods Not Allowed : 유효하지 않은 요청
 * 409 Conflict : 리소스 충돌(중복)
 *
 * 5xx 서버 오류
 * 500 Internal Server Error : 서버 오류 발생
 * 503 Service Unavailable : 서비스 사용 불가
 * </pre>
 */

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 공통
    COMMON_BAD_REQUEST_400(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    COMMON_INTERNAL_ERROR_500(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생하였습니다."),

    // JWT 관련
    JWT_BAD_TOKEN_400(HttpStatus.BAD_REQUEST, "잘못된 JWT 토큰입니다."),
    JWT_UNAUTHORIZED_401(HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다."),
    JWT_NOT_FOUND_404(HttpStatus.NOT_FOUND, "유효한 JWT 토큰이 없습니다."),
    JWT_NOT_ALLOWED_405(HttpStatus.METHOD_NOT_ALLOWED, "지원되지 않는 JWT 토큰입니다."),

    // Member 관련
    MEMBER_LOGINID_NOT_FOUND_404(HttpStatus.NOT_FOUND, "회원 정보를 찾을 수 없습니다."),
    MEMBER_LOGINID_DUPLICATION_409(HttpStatus.CONFLICT, "이미 가입된 이메일입니다."),


    // Project 관련
    PROJECT_NOT_FOUND_404(HttpStatus.NOT_FOUND, "요청한 프로젝트 ID가 없습니다."),
    PROJECT_DUPLICATION_409(HttpStatus.CONFLICT, "이미 등록된 프로젝트 ID 입니다."),

    // ProjectMember 관련
    PROJECT_MEMBER_DUPLICATION_409(HttpStatus.CONFLICT, "이미 프로젝트에 참가되어 있는 회원입니다."),

    // Kanban 관련
    KANBAN_BUCKET_NOT_FOUND_404(HttpStatus.NOT_FOUND, "요청한 칸반보드 ID가 없습니다."),
    KANBAN_CARD_NOT_FOUND_404(HttpStatus.NOT_FOUND, "요청한 칸반카드 ID가 없습니다."),
    KANBAN_BUCKET_DUPLICATION_409(HttpStatus.CONFLICT, "이미 등록된 보드 ID 입니다."),
    KANBAN_CARD_DUPLICATION_409(HttpStatus.CONFLICT, "이미 등록된 카드 ID 입니다."),

    // Document 관련


    /**
     * 해당 주석 위로 enum 코드 추가 바랍니다.
     * 코드 추가시 간편하게 진행하기 위해 생성한 미사용 코드입니다. 사용하지마세요.
     * 생성이유 : enum 마지막 요소에 ; 을 입력해야하기에, 끝부분에 추가하게 될 경우 ; 을 재입력해야함
     */
    DO_NOT_USED(null, null);


    private final HttpStatus httpStatus;
    private final String message;
}
