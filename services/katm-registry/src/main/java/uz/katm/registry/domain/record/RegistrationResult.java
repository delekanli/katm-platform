package uz.katm.registry.domain.record;

/** Результат регистрации организации: сгенерированные логин/пароль + код/сообщение операции. */
public record RegistrationResult(
        String login,
        String password,
        String result,
        String message
) {
}
