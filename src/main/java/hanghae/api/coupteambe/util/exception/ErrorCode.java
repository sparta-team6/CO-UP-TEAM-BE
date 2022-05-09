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
    COMMON_INTERNAL_ERROR_500(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생하였습니다."),
    COMMON_BAD_REQUEST_400(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),


    // Member 관련
    LOGINID_DUPLICATION_409(HttpStatus.CONFLICT, "이미 가입된 이메일입니다."),


    // Project 관련


    // Kanban 관련


    // Document 관련


    DO_NOT_USED(null,null);


    private final HttpStatus httpStatus;
    private final String message;
}
