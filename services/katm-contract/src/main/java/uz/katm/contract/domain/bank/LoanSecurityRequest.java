package uz.katm.contract.domain.bank;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record LoanSecurityRequest(
        @NotBlank String guaranteeId,
        @NotBlank String claimId,
        @NotBlank String ownerId,
        @NotBlank String agreementNumber,
        @NotNull LocalDate agreementDate,
        @NotBlank String guaranteeType,
        @NotBlank String contractNumber,
        @NotNull LocalDate contractDate,
        @NotNull @Positive Long summa,
        @NotBlank String currency,
        String name,
        String description,
        @NotBlank String status,
        @NotBlank String loanSubject,
        @NotNull LocalDate date
) {}
