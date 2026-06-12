package uz.katm.system.domain.dto;

/** Регистрация ошибки онлайн-запроса (DATAS.PKG_ONLINE.SET_ONLINE_REQUEST_ERROR). */
public record OnlineRequestErrorRequest(
        String tin,
        String pin,
        String docSeries,
        String docNumber,
        String errorCode,
        String errorMessage
) {
}
