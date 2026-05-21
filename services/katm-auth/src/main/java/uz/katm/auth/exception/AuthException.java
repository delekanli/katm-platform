package uz.katm.auth.exception;

import org.springframework.http.HttpStatus;
import uz.katm.common.exception.BusinessException;

public class AuthException extends BusinessException {

    public AuthException(String message) {
        super("AUTH_ERROR", message, HttpStatus.UNAUTHORIZED);
    }

    public AuthException(String message, Throwable cause) {
        super("AUTH_ERROR", message, HttpStatus.UNAUTHORIZED);
        initCause(cause);
    }
}
