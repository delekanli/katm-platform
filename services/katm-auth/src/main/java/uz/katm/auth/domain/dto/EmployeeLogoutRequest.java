package uz.katm.auth.domain.dto;

import jakarta.validation.constraints.NotNull;

/** Запрос завершения сессии сотрудника. */
public record EmployeeLogoutRequest(
        @NotNull Integer userId,
        String sessionId
) {
}
