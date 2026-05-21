package uz.katm.common.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import uz.katm.common.kafka.KafkaTopics;

@Configuration
public class KafkaConfig {

    @Bean public NewTopic claimCreatedTopic() {
        return TopicBuilder.name(KafkaTopics.CLAIM_CREATED).partitions(3).replicas(1).build();
    }
    @Bean public NewTopic claimUpdatedTopic() {
        return TopicBuilder.name(KafkaTopics.CLAIM_UPDATED).partitions(3).replicas(1).build();
    }
    @Bean public NewTopic claimStatusChangedTopic() {
        return TopicBuilder.name(KafkaTopics.CLAIM_STATUS_CHANGED).partitions(3).replicas(1).build();
    }
    @Bean public NewTopic contractCreatedTopic() {
        return TopicBuilder.name(KafkaTopics.CONTRACT_CREATED).partitions(3).replicas(1).build();
    }
    @Bean public NewTopic contractSignedTopic() {
        return TopicBuilder.name(KafkaTopics.CONTRACT_SIGNED).partitions(3).replicas(1).build();
    }
    @Bean public NewTopic reportRequestedTopic() {
        return TopicBuilder.name(KafkaTopics.REPORT_REQUESTED).partitions(3).replicas(1).build();
    }
    @Bean public NewTopic notificationEmailTopic() {
        return TopicBuilder.name(KafkaTopics.NOTIFICATION_EMAIL).partitions(3).replicas(1).build();
    }
    @Bean public NewTopic notificationSmsTopic() {
        return TopicBuilder.name(KafkaTopics.NOTIFICATION_SMS).partitions(3).replicas(1).build();
    }
    @Bean public NewTopic mipRequestTopic() {
        return TopicBuilder.name(KafkaTopics.MIP_REQUEST).partitions(3).replicas(1).build();
    }
    @Bean public NewTopic mipResponseTopic() {
        return TopicBuilder.name(KafkaTopics.MIP_RESPONSE).partitions(3).replicas(1).build();
    }
}
