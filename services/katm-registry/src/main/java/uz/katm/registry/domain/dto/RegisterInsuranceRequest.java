package uz.katm.registry.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/** Запрос регистрации страховой организации (PKG_INSURANCES.ADD_INSURANCE). */
public record RegisterInsuranceRequest(
        @NotNull Integer type,
        @NotBlank String name,
        @NotNull LocalDate contractStartDate,
        @NotBlank String region,
        @NotBlank String localRegion,
        @NotBlank String address
) {
}
