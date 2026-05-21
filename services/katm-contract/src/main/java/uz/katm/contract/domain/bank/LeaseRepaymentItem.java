package uz.katm.contract.domain.bank;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record LeaseRepaymentItem(
        @NotNull LocalDate date,
        @NotBlank String currency,
        @NotBlank String leasingType,
        Integer amortization,
        @NotNull @Positive Long amount,
        String name,
        @NotBlank String status,
        String details,
        String objectId
) {}
