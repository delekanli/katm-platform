package uz.katm.contract.domain.retailer;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PlanItem(
        @NotNull LocalDate payDate,
        @NotNull Long mainAmount,
        @NotNull Long percentAmount
) {}
