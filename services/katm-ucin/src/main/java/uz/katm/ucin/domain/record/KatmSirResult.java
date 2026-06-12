package uz.katm.ucin.domain.record;

/** Результат поиска клиента в КАТМ СИР по данным лица (SMS_NOTIFICATION.GET_CLIENT_ID). */
public record KatmSirResult(
        String clientId,
        Integer notificationEnabled,
        Integer lang,
        String msisdn,
        Integer errorCode
) {
}
