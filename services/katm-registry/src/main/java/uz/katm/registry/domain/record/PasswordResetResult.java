package uz.katm.registry.domain.record;

/** Результат сброса пароля: новый пароль + код/сообщение операции. */
public record PasswordResetResult(
        String password,
        String result,
        String message
) {
}
