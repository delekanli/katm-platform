package uz.katm.auth.domain.dto;

import jakarta.validation.constraints.NotBlank;

/** Запрос авторизации сотрудника. */
public record EmployeeLoginRequest(
        @NotBlank String login,
        @NotBlank String password
) {
}
