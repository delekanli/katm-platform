package uz.katm.ucin.domain.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

/** Запрос кредитного отчёта UCIN (DATAS.PKG_WEB_INDIVIDUAL.GET_CREDIT_REPORT). */
public record CreditReportRequest(
        @NotBlank String clientId,
        @NotBlank String claimId,
        LocalDate claimDate,
        Integer reportId,
        String ip,
        String lang
) {
}
