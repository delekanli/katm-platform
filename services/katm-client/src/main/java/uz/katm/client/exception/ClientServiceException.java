package uz.katm.client.exception;

import org.springframework.http.HttpStatus;
import uz.katm.common.exception.BusinessException;

public class ClientServiceException extends BusinessException {

    public ClientServiceException(String procedureCode, String message) {
        super(procedureCode, message, HttpStatus.BAD_REQUEST);
    }

    public String getProcedureCode() {
        return getCode();
    }
}
