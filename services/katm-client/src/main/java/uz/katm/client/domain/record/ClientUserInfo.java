package uz.katm.client.domain.record;

/**
 * Учётная запись пользователя клиент-портала (site.r_users).
 * Перенос ClientUserDto монолита БЕЗ поля password — пароль не отдаётся через REST.
 */
public record ClientUserInfo(
        Integer id,
        String login,
        String position,
        String firstName,
        String lastName,
        String middleName,
        String tin
) {
}
