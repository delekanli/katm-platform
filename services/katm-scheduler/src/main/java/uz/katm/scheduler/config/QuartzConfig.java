package uz.katm.scheduler.config;

import org.jspecify.annotations.NullMarked;
import org.quartz.*;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import uz.katm.scheduler.job.EgovResendJob;
import uz.katm.scheduler.job.SmsSendJob;

@Configuration
public class QuartzConfig {

    @Bean
    public SpringBeanJobFactory springBeanJobFactory(ApplicationContext applicationContext) {
        AutowiringSpringBeanJobFactory factory = new AutowiringSpringBeanJobFactory();
        factory.setApplicationContext(applicationContext);
        return factory;
    }

    // ── SmsSendJob: every 30 seconds ─────────────────────────────────────────

    @Bean
    public JobDetail smsSendJobDetail() {
        return JobBuilder.newJob(SmsSendJob.class)
                .withIdentity("smsSendJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger smsSendTrigger(@Qualifier("smsSendJobDetail") JobDetail smsSendJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(smsSendJobDetail)
                .withIdentity("smsSendTrigger")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(30)
                        .repeatForever())
                .build();
    }

    // ── EgovResendJob: hourly ────────────────────────────────────────────────

    @Bean
    public JobDetail egovResendJobDetail() {
        return JobBuilder.newJob(EgovResendJob.class)
                .withIdentity("egovResendJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger egovResendTrigger(@Qualifier("egovResendJobDetail") JobDetail egovResendJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(egovResendJobDetail)
                .withIdentity("egovResendTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 * * * ?"))
                .build();
    }

    // ── Autowiring factory ────────────────────────────────────────────────────

    static class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory {

        private AutowireCapableBeanFactory beanFactory;

        @Override
        public void setApplicationContext(ApplicationContext context) {
            this.beanFactory = context.getAutowireCapableBeanFactory();
        }

        @Override
        @NullMarked
        protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
            Object job = super.createJobInstance(bundle);
            beanFactory.autowireBean(job);
            return job;
        }
    }
}
