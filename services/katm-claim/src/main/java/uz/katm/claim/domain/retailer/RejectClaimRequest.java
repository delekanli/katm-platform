package uz.katm.claim.domain.retailer;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

/** Отклонение заявки ритейлера/организации (PKG_RETAILERS.REJECT_CLAIM / REJECT_LEGAL_CLAIM). */
public record RejectClaimRequest(
        @NotBlank String claimId,
        LocalDate rejDate,
        String reasonId,
        String reason,
        Integer isUpdate
) {
}
