package uz.katm.notification.domain.record;

/** Тип уведомления (datas.SMS_TEMPLATE: sms_type + comment). */
public record NotificationType(
        Integer id,
        String name
) {
}
