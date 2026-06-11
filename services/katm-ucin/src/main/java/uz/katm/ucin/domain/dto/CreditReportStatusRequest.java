package uz.katm.ucin.domain.dto;

import jakarta.validation.constraints.NotBlank;

/** Запрос статуса/получения готового кредитного отчёта UCIN (GET_CREDIT_REPORT_STATUS). */
public record CreditReportStatusRequest(
        @NotBlank String claimId,
        @NotBlank String token
) {
}
