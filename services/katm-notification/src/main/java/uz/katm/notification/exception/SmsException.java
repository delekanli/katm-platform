package uz.katm.notification.exception;

import org.springframework.http.HttpStatus;
import uz.katm.common.exception.BusinessException;

public class SmsException extends BusinessException {

    public SmsException(String message, Throwable cause) {
        super("SMS_SEND_ERROR", message, HttpStatus.BAD_GATEWAY);
        initCause(cause);
    }
}
