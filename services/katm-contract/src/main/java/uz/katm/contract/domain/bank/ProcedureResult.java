package uz.katm.contract.domain.bank;

public record ProcedureResult(String code, String message) {
    private static final String SUCCESS_CODE = "0";

    public boolean isSuccess() {
        return SUCCESS_CODE.equals(code);
    }
}
