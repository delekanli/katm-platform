package uz.katm.notification.domain.record;

/** Результат ретрансляции уведомления во внешний UCIN NOTIFICATION SERVICE. */
public record UcinNotificationResult(boolean delivered, String message) {

    public static UcinNotificationResult ok() {
        return new UcinNotificationResult(true, "Уведомление доставлено в UCIN");
    }

    public static UcinNotificationResult failed(String message) {
        return new UcinNotificationResult(false, message);
    }
}
