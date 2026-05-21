package uz.katm.report.exception;

import org.springframework.http.HttpStatus;
import uz.katm.common.exception.BusinessException;

public class CreditServiceException extends BusinessException {

    public CreditServiceException(String procedureCode, String message) {
        super(procedureCode, message, HttpStatus.BAD_REQUEST);
    }

    public String getProcedureCode() {
        return getCode();
    }
}
