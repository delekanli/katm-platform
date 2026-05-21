package uz.katm.notification.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import uz.katm.common.kafka.KafkaTopics;
import uz.katm.notification.service.NotificationService;
import uz.katm.notification.service.SmsOutboxService;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationKafkaConsumer {

    private final NotificationService notificationService;
    private final SmsOutboxService smsOutboxService;

    @KafkaListener(topics = KafkaTopics.CLIENT_CREATED, groupId = "notification-service")
    public void onClientCreated(String payload) {
        log.info("Received client.created event: {}", payload);
    }

    @KafkaListener(topics = KafkaTopics.CREDIT_UPDATED, groupId = "notification-service")
    public void onCreditUpdated(String payload) {
        log.info("Received credit.updated event: {}", payload);
        notificationService.onCreditUpdate(payload);
    }

    @KafkaListener(topics = KafkaTopics.REPORT_READY, groupId = "notification-service")
    public void onReportReady(String payload) {
        log.info("Received report.ready event: {}", payload);
        notificationService.onReportReady(payload);
    }

    @KafkaListener(topics = KafkaTopics.JOB_SMS_SEND, groupId = "notification-service")
    public void onSmsSendJob(String payload) {
        log.info("SMS outbox job triggered");
        smsOutboxService.processOutbox();
    }
}
