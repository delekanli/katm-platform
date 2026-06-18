package uz.katm.notification.domain.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Уведомление субъекту о подключении услуги Freeze. Перенос FreezeNotificationParams монолита.
 * Поля сериализуются с теми же ключами (pinfl/textUz/textRu/textEn) — это же тело уходит в UCIN.
 */
public record FreezeNotificationRequest(
        @NotBlank String pinfl,
        String textUz,
        String textRu,
        String textEn
) {
}
