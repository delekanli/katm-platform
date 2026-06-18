package uz.katm.notification.domain.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Уведомление субъекту об оформлении кредита. Перенос CreditNotificationParams монолита.
 * Поля сериализуются с теми же ключами — это же тело уходит в UCIN.
 */
public record CreditNotificationRequest(
        @NotBlank String pinfl,
        String orgName,
        String contractNumber,
        String contractAmount,
        String contractTerm,
        String contractType
) {
}
