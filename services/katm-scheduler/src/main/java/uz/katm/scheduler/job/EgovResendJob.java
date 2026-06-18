package uz.katm.scheduler.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import uz.katm.common.kafka.KafkaTopics;

/**
 * Триггер переотправки неотправленных отчётов в E-GOV (раз в час).
 * Перенос gov.uz.ucin.katm.api.scheduler.ResendEgovReportsScheduler — сама логика
 * выполняется в katm-egov по событию {@link KafkaTopics#JOB_EGOV_RESEND}.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EgovResendJob implements Job {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void execute(JobExecutionContext context) {
        log.info("Triggering E-GOV reports resend job");
        kafkaTemplate.send(KafkaTopics.JOB_EGOV_RESEND, "TRIGGER");
        log.info("E-GOV resend job triggered via Kafka");
    }
}
