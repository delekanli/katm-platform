package uz.katm.report.domain.record;

import jakarta.validation.constraints.NotBlank;

public record CreditReportRequest(
        int isLegal,
        @NotBlank String claimId,
        int reportId,
        @NotBlank String ip,
        String mipResponse,
        int isShow,
        String lang,
        String juridicalStatus,
        String loanSubject,
        String personalCode,
        String inn,
        String ownerId,
        // Формат выдачи: 0=XML, 1=JSON, 2=HTML (Constants.REPORT_*). Отсутствует в JSON → 0 (XML).
        int reportFormat
) {}
