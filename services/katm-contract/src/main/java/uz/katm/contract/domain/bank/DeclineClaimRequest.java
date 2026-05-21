package uz.katm.contract.domain.bank;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record DeclineClaimRequest(
        @NotBlank String claimId,
        @NotNull LocalDate date,
        @NotBlank String declineNumber,
        @NotBlank String declineReason,
        String declineReasonNote
) {}
