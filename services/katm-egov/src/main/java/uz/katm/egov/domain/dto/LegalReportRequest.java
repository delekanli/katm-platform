package uz.katm.egov.domain.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Запрос кредитного отчёта по юрлицу через E-GOV
 * (DATAS.PKG_WEB_INDIVIDUAL.EGOV_LEGAL_GET_CREDIT_REPORT).
 */
public record LegalReportRequest(
        @NotBlank String inn,
        String region,
        String localRegion,
        String fullName,
        String postAddress,
        String phone,
        String resident,
        Integer clientTypeId,
        String ownerForm,
        Integer government,
        String okpo,
        String hbranch,
        String industry,
        String ip
) {
}
