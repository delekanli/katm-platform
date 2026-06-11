package uz.katm.egov.domain.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

/**
 * Запрос кредитного отчёта по физлицу через E-GOV
 * (DATAS.PKG_WEB_INDIVIDUAL.EGOV_GET_CREDIT_REPORT).
 */
public record IndividualReportRequest(
        String pinfl,
        String identityCardSerial,
        String identityCardNumber,
        LocalDate identityCardDate,
        String inn,
        String region,
        String localRegion,
        String familyName,
        String name,
        String patronymic,
        LocalDate dateBirth,
        String phone,
        String postAddress,
        String liveAddress,
        String resident,
        String sex,
        String ip,
        @NotBlank String claimId,
        LocalDate claimDate
) {
}
