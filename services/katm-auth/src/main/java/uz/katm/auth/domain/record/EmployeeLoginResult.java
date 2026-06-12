package uz.katm.auth.domain.record;

/** Результат авторизации сотрудника (DATAS.PKG_USERS.USER_AUTHORIZATION). */
public record EmployeeLoginResult(
        String result,
        String message,
        Integer userId,
        Integer roleId,
        String roleName
) {
}
