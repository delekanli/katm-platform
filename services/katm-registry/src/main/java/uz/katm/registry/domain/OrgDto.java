package uz.katm.registry.domain;

import java.time.LocalDate;

/**
 * Элемент списка организаций (страховая/ритейлер).
 * Поле {@code tin} заполняется только для ритейлеров (для страховых — null).
 */
public record OrgDto(
        String code,
        String name,
        String region,
        String localRegion,
        String address,
        LocalDate contractStartDate,
        LocalDate contractEndDate,
        String status,
        String mode,
        String tin
) {
}
