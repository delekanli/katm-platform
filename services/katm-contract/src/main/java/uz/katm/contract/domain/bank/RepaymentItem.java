package uz.katm.contract.domain.bank;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RepaymentItem(
        @NotNull LocalDate date,
        @NotBlank String account,
        @NotNull Long debit,
        @NotNull Long credit,
        @NotNull Long startBalance,
        @NotNull Long endBalance
) {}
