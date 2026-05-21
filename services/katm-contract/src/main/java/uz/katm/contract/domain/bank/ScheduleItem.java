package uz.katm.contract.domain.bank;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record ScheduleItem(
        @NotNull LocalDate date,
        @NotBlank String currency,
        @NotNull @Positive Long amount,
        @NotNull @Positive Long percent
) {}
