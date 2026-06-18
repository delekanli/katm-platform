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

    /** Возвращает копию с заменённым телом отчёта (после конвертации формата). */
    public CreditReportResponse withReport(byte[] newReport) {
        return new CreditReportResponse(code, message, newReport, token);
    }
}
