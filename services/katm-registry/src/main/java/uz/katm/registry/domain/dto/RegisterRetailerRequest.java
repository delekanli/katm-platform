package uz.katm.registry.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/** Запрос регистрации ритейлера (PKG_RETAILERS.ADD_RETAILER). */
public record RegisterRetailerRequest(
        @NotNull Integer type,
        @NotBlank String name,
        @NotNull LocalDate contractStartDate,
        @NotBlank String region,
        @NotBlank String localRegion,
        @NotBlank String address,
        @NotBlank String tin
) {
}
