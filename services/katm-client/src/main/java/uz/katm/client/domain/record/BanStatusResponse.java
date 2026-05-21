package uz.katm.client.domain.record;

public record BanStatusResponse(
        String code,
        String message,
        Integer isBanned
) {
    private static final String SUCCESS_CODE = "0";

    public boolean isSuccess() {
        return SUCCESS_CODE.equals(code);
    }

    public boolean banned() {
        return isBanned != null && isBanned == 1;
    }
}
