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
public class SmsSendJob implements Job {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void execute(JobExecutionContext context) {
        log.info("Starting SMS send job");
        kafkaTemplate.send(KafkaTopics.JOB_SMS_SEND, "TRIGGER");
        log.info("SMS send job triggered via Kafka");
    }
}
