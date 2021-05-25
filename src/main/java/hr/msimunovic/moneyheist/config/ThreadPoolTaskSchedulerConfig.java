package hr.msimunovic.moneyheist.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;

import java.util.concurrent.TimeUnit;

@Configuration
@ComponentScan(
        basePackages="hr.msimunovic.moneyheist")
public class ThreadPoolTaskSchedulerConfig {

    @Value("${member.levelUpTime}")
    private Long levelUpTime;

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler(){

        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(5);
        threadPoolTaskScheduler.setRemoveOnCancelPolicy(true);
        threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");

        return threadPoolTaskScheduler;
    }

    @Bean
    public PeriodicTrigger periodicFixedDelayTrigger() {
        PeriodicTrigger periodicTrigger = new PeriodicTrigger(levelUpTime, TimeUnit.SECONDS);
        periodicTrigger.setFixedRate(true);
        periodicTrigger.setInitialDelay(levelUpTime);
        return periodicTrigger;
    }
}