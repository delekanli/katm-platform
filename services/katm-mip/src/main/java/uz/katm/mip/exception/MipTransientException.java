package uz.katm.mip.exception;

import org.springframework.http.HttpStatus;
import uz.katm.common.exception.BusinessException;

/**
 * Временный (повторяемый) сбой при обращении к госсервису ПЦД/МИП:
 * сетевой таймаут, обрыв соединения, недоступность узла.
 * <p>Resilience4j настроен повторять вызовы именно на этом исключении;
 * после исчерпания попыток оно маппится в HTTP 502 через {@code GlobalExceptionHandler}.
 */
public class MipTransientException extends BusinessException {

    public MipTransientException(String message, Throwable cause) {
        super("MIP_PCD_TRANSIENT", message, HttpStatus.BAD_GATEWAY);
        initCause(cause);
    }
}
