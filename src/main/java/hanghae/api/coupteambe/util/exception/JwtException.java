package hanghae.api.coupteambe.util.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class JwtException  extends RuntimeException {

    private final HttpStatus httpStatus;

    public JwtException(String message) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }
}
