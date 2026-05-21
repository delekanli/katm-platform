package uz.katm.notification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class RetryConfig {

    @Bean
    public RetryTemplate retryTemplate() {
        FixedBackOffPolicy backOff = new FixedBackOffPolicy();
        backOff.setBackOffPeriod(5_000);

        SimpleRetryPolicy policy = new SimpleRetryPolicy();
        policy.setMaxAttempts(3);

        RetryTemplate template = new RetryTemplate();
        template.setBackOffPolicy(backOff);
        template.setRetryPolicy(policy);
        return template;
    }
}
