package uz.katm.notification.domain.record;

import java.time.LocalDateTime;

/** Элемент истории уведомлений клиента (DATAS.SMS_NOTIFICATION.subscribtion_history). */
public record NotificationHistoryItem(
        LocalDateTime date,
        String msisdn,
        String text
) {
}
