package uz.katm.report.domain.record;

import jakarta.validation.constraints.NotBlank;

/**
 * Запрос системного диспетчера отчётов (перенос ReportParams монолитного getReport).
 * reportId выбирает источник данных; остальные поля — параметры конкретных отчётов
 * (number — гос. номер/кадастр, subReportId — подтип, year/period — период, signature — ЭЦП для E-Invoice).
 */
public record SystemReportRequest(
        int reportId,
        @NotBlank String claimId,
        String ip,
        String lang,
        String ownerId,
        String loanSubject,
        String number,
        Integer subReportId,
        Integer year,
        Integer period,
        String signature,
        int reportFormat
) {
}
