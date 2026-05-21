package uz.katm.notification.domain.record;

public record OtpResponse(
        String subscriptionStatus,
        String otp
) {}
