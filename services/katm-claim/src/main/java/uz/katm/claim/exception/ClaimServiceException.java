package uz.katm.claim.exception;

import org.springframework.http.HttpStatus;
import uz.katm.common.exception.BusinessException;

public class ClaimServiceException extends BusinessException {

    public ClaimServiceException(String procedureCode, String message) {
        super(procedureCode, message, HttpStatus.BAD_REQUEST);
    }

    public ClaimServiceException(String message) {
        super("CLAIM_ERROR", message, HttpStatus.BAD_REQUEST);
    }

    public String getProcedureCode() {
        return getCode();
    }
}
