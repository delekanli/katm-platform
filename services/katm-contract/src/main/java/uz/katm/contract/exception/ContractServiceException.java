package uz.katm.contract.exception;

import org.springframework.http.HttpStatus;
import uz.katm.common.exception.BusinessException;

public class ContractServiceException extends BusinessException {

    public ContractServiceException(String message) {
        super("CONTRACT_ERROR", message, HttpStatus.BAD_REQUEST);
    }

    public ContractServiceException(String code, String message) {
        super(code, message, HttpStatus.BAD_REQUEST);
    }

    public String getProcedureCode() {
        return getCode();
    }
}
