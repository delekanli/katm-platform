package uz.katm.claim.domain.insurance;

public record ClaimResult(
        String code,
        String message,
        Integer initialId
) {
    private static final String SUCCESS_CODE = "0";

    public boolean isSuccess() {
        return SUCCESS_CODE.equals(code);
    }
}
