package uz.katm.scheduler.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import uz.katm.common.kafka.KafkaTopics;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreditSyncJob implements Job {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void execute(JobExecutionContext context) {
        log.info("Starting daily credit sync job");
        kafkaTemplate.send(KafkaTopics.JOB_CREDIT_SYNC, "TRIGGER");
        log.info("Credit sync job triggered via Kafka");
    }
}
