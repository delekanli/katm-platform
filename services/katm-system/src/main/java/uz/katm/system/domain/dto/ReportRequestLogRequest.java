package uz.katm.system.domain.dto;

import java.time.LocalDate;

/** Лог запроса отчёта (DATAS.PKG_ONLINE.ONLINE_REPORTS_REQUEST). */
public record ReportRequestLogRequest(
        String head,
        String code,
        String reportType,
        String agreementId,
        LocalDate agreementDate,
        String claimId,
        String status,
        String response
) {
}
