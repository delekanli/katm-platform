package uz.katm.contract.domain.bank;

import jakarta.validation.constraints.NotBlank;

public record GetClaimRequest(
        @NotBlank String claimId
) {}
