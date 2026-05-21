package uz.katm.claim.domain.bank;

import jakarta.validation.constraints.NotBlank;

public record GetClaimRequest(
        @NotBlank String claimId
) {}
