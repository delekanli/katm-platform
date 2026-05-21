package uz.katm.common.kafka;

public final class KafkaTopics {
    private KafkaTopics() {}

    // Client events
    public static final String CLIENT_CREATED       = "client.created";
    public static final String CREDIT_UPDATED       = "credit.updated";

    // Claim events
    public static final String CLAIM_CREATED        = "claim.created";
    public static final String CLAIM_UPDATED        = "claim.updated";
    public static final String CLAIM_STATUS_CHANGED = "claim.status.changed";

    // Contract events
    public static final String CONTRACT_CREATED     = "contract.created";
    public static final String CONTRACT_SIGNED      = "contract.signed";

    // Report events
    public static final String REPORT_REQUESTED     = "report.requested";
    public static final String REPORT_READY         = "report.ready";

    // Notifications
    public static final String NOTIFICATION_EMAIL   = "notification.email";
    public static final String NOTIFICATION_SMS     = "notification.sms";

    // MIP integration
    public static final String MIP_REQUEST          = "katm.mip.request";
    public static final String MIP_RESPONSE         = "katm.mip.response";

    // Scheduler triggers
    public static final String JOB_CREDIT_SYNC      = "jobs.credit.sync";
    public static final String JOB_SMS_SEND         = "jobs.sms.send";
}
