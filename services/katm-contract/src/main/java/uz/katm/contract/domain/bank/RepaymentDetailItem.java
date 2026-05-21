package uz.katm.contract.domain.bank;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record RepaymentDetailItem(
        @NotBlank String payType,
        @NotBlank String docNum,
        @NotBlank String docType,
        @NotNull LocalDate docDate,
        @NotBlank String currency,
        String nameA,
        String branchA,
        @NotBlank String accountA,
        String nameB,
        String branchB,
        @NotBlank String accountB,
        @NotNull @Positive Long summa,
        String purpose,
        String coaA,
        String coaB,
        String paymentId
) {}
