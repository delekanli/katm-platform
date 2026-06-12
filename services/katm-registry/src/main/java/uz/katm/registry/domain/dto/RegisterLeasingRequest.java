package uz.katm.registry.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/** Запрос регистрации лизинговой организации (DATAS.PKG_LEASING.ADD_LEASING). */
public record RegisterLeasingRequest(
        @NotBlank String name,
        @NotNull LocalDate contractStartDate,
        LocalDate contractEndDate,
        @NotBlank String region,
        @NotBlank String localRegion,
        @NotBlank String tin
) {
}
