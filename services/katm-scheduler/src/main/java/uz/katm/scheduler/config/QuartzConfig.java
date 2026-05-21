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
import uz.katm.scheduler.job.CreditSyncJob;
import uz.katm.scheduler.job.SmsSendJob;

@Configuration
public class QuartzConfig {

    @Bean
    public SpringBeanJobFactory springBeanJobFactory(ApplicationContext applicationContext) {
        AutowiringSpringBeanJobFactory factory = new AutowiringSpringBeanJobFactory();
        factory.setApplicationContext(applicationContext);
        return factory;
    }

    // ── CreditSyncJob: daily at 01:00 ────────────────────────────────────────

    @Bean
    public JobDetail creditSyncJobDetail() {
        return JobBuilder.newJob(CreditSyncJob.class)
                .withIdentity("creditSyncJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger creditSyncTrigger(@Qualifier("creditSyncJobDetail") JobDetail creditSyncJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(creditSyncJobDetail)
                .withIdentity("creditSyncTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 1 * * ?"))
                .build();
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
