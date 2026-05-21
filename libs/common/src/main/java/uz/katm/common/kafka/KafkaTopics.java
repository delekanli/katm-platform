package uz.katm.common.kafka;

public final class KafkaTopics {
    private KafkaTopics() {}

    // Domain events
    public static final String CLIENT_CREATED  = "client.created";
    public static final String CREDIT_UPDATED  = "credit.updated";
    public static final String REPORT_READY    = "report.ready";

    // MIP integration
    public static final String MIP_REQUEST     = "katm.mip.request";
    public static final String MIP_RESPONSE    = "katm.mip.response";

    // Scheduler triggers
    public static final String JOB_CREDIT_SYNC = "jobs.credit.sync";
    public static final String JOB_SMS_SEND    = "jobs.sms.send";
}
