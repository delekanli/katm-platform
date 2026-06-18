package uz.katm.egov.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import uz.katm.common.kafka.KafkaTopics;
import uz.katm.egov.service.EgovResendService;

/** Слушает триггеры джобов katm-scheduler для E-GOV. */
@Slf4j
@Component
@RequiredArgsConstructor
public class EgovKafkaConsumer {

    private final EgovResendService egovResendService;

    @KafkaListener(topics = KafkaTopics.JOB_EGOV_RESEND, groupId = "egov-service")
    public void onEgovResendJob(String payload) {
        log.info("E-GOV resend job triggered");
        egovResendService.resendReports();
    }
}
