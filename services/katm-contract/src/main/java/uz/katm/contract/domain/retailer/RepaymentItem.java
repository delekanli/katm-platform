package uz.katm.contract.domain.retailer;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RepaymentItem(
        @NotNull LocalDate payDate,
        @NotNull Long mainAmount,
        @NotNull Long percentAmount
) {}
