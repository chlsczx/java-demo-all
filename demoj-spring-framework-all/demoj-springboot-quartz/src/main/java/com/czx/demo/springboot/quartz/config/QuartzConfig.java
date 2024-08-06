package com.czx.demo.springboot.quartz.config;

import com.czx.demo.springboot.quartz.quarz.job.MyJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;

// @Configuration
// @EnableScheduling
public class QuartzConfig {

    @Bean
    public JobDetail jobDetail() {
        return JobBuilder
                .newJob(MyJob.class)
                .storeDurably()
                .withIdentity("myJob1", "group1")
                .usingJobData("count", 1)
                .build();
    }

    @Bean
    public Trigger trigger() {
        String cronExpression = "0/2 * * * * ? *"; // sec min hour day month dow year

        return TriggerBuilder.newTrigger()
                .withIdentity("trigger1", "group1")
                .forJob(jobDetail())
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .build();

    }


}
