package uz.katm.registry.domain.dto;

import jakarta.validation.constraints.NotBlank;

/** Запрос сброса пароля организации по логину (PKG_USERS.ONLINE_PASSWORD_CHANGE). */
public record PasswordResetRequest(
        @NotBlank String login
) {
}
