package uz.katm.egov.domain.dto;

import jakarta.validation.constraints.NotBlank;

/** Обновление статуса заявки E-GOV (DATAS.PKG_WEB_INDIVIDUAL.EGOV_CLAIM_UPDATE_STATUS). */
public record ClaimStatusUpdateRequest(
        @NotBlank String claimStatus,
        String currentNode
) {
}
