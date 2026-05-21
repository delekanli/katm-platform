package uz.katm.report.domain.record;

public record CreditReportResponse(
        String code,
        String message,
        byte[] report,
        String token
) {
    private static final String SUCCESS_CODE = "0";

    public boolean isSuccess() {
        return SUCCESS_CODE.equals(code);
    }

    public boolean isPending() {
        return "05050".equals(code);
    }
}
