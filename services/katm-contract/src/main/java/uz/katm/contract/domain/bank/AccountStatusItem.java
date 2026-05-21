package uz.katm.contract.domain.bank;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AccountStatusItem(
        @NotBlank String coa,
        @NotBlank String account,
        LocalDate dateOpen,
        LocalDate dateClose,
        @NotNull LocalDate date
) {}
