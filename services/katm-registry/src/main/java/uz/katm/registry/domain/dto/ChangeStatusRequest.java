package uz.katm.registry.domain.dto;

import jakarta.validation.constraints.NotNull;

/** Запрос смены статуса организации. status — новый статус, mode — признак тестового режима (IN_TEST). */
public record ChangeStatusRequest(
        @NotNull Integer status,
        @NotNull Integer mode
) {
}
