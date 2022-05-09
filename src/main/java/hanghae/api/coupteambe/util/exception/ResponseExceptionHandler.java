package hanghae.api.coupteambe.util.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * 참고사이트
 * <a href="https://blog.naver.com/PostView.naver?blogId=writer0713&logNo=221605253778&parentCategoryNo=&categoryNo=83&viewDate=&isShowPopularPosts=true&from=search">https://blog.naver.com/PostView.naver?blogId=writer0713&logNo=221605253778&parentCategoryNo=&categoryNo=83&viewDate=&isShowPopularPosts=true&from=search</a>
 * <a href="https://dev-setung.tistory.com/16">https://dev-setung.tistory.com/16</a>
 */

@RestControllerAdvice
@Slf4j
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({RequestException.class, JwtException.class})
    protected ResponseEntity<String> handleCustomException(RequestException e) {

        log.debug("Exception : '{}'", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
    }
}
