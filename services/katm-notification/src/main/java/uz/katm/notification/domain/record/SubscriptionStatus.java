package uz.katm.notification.domain.record;

public enum SubscriptionStatus {
    INACTIVE(0),
    ACTIVE(1),
    SUSPENDED(2),
    FROZEN(3),
    UNKNOWN(-1);

    private final int code;

    SubscriptionStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static SubscriptionStatus fromCode(int code) {
        for (SubscriptionStatus s : values()) {
            if (s.code == code) return s;
        }
        return UNKNOWN;
    }
}
