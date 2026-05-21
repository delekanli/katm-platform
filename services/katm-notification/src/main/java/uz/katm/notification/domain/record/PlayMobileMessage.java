package uz.katm.notification.domain.record;

public record PlayMobileMessage(
        String recipient,
        String messageId,
        PlayMobileSms sms
) {}
