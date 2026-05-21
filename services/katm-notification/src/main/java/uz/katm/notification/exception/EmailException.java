package uz.katm.notification.exception;

import org.springframework.http.HttpStatus;
import uz.katm.common.exception.BusinessException;

public class EmailException extends BusinessException {

    public EmailException(String message, Throwable cause) {
        super("EMAIL_SEND_ERROR", message, HttpStatus.BAD_GATEWAY);
        initCause(cause);
    }
}
