package uz.katm.claim.domain.insurance;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record InitiateClaimRequest(
        @NotBlank String claimId,
        @NotNull LocalDate claimDate,
        @NotBlank String agreementId,
        @NotNull LocalDate agreementDate,
        @NotBlank String docSeries,
        @NotBlank String docNumber,
        @NotBlank String region,
        @NotBlank String localRegion,
        @NotBlank String address,
        @NotBlank String pinfl,
        @NotBlank String phone,
        @Positive long creditAmount,
        @NotBlank String currency,
        @NotNull LocalDate creditEndDate
) {}
