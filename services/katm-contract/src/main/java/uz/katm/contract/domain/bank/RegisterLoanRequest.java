package uz.katm.contract.domain.bank;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record RegisterLoanRequest(
        @NotBlank String claimId,
        @NotBlank String contractId,
        @NotBlank @Size(min = 9, max = 9) @Pattern(regexp = "[0-9]+") String inn,
        @NotBlank String nibbd,
        @NotNull LocalDate date,
        @NotBlank String creditType,
        String creditObject,
        @NotNull LocalDate creditDateBegin,
        @NotNull LocalDate creditDateEnd,
        @NotNull @Positive Long summa,
        @NotBlank String currency,
        Double procent,
        String juridicNumber,
        String supply,
        String quality,
        String urgency,
        String hbranch,
        String creditActivity,
        String reason,
        String founder
) {}
