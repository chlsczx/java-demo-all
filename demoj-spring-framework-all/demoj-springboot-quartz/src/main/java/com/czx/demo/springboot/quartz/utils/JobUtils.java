package com.czx.demo.springboot.quartz.utils;

import com.czx.demo.springboot.quartz.model.QuartzBean;
import org.jetbrains.annotations.NotNull;
import org.quartz.*;

import java.util.Objects;

public class JobUtils {

    /**
     * Create job
     *
     * @param scheduler  scheduler
     * @param quartzBean quartzBean
     */
    public static void createJob(@NotNull Scheduler scheduler,
                                 @NotNull QuartzBean quartzBean)
            throws SchedulerException {
        Class<? extends Job> jobClass = quartzBean.getJobClass();

        String jobName = Objects.requireNonNull(quartzBean.getJobName()),
                jobGroup = quartzBean.getJobGroup(),
                triggerName = Objects.requireNonNullElse(quartzBean.getTriggerName(), determineTriggerName(quartzBean)),
                triggerGroup = quartzBean.getTriggerGroup(),
                cronExpression = Objects.requireNonNull(quartzBean.getCronExpression());

        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

        JobDetail jobDetail = JobBuilder
                .newJob(jobClass)
                .storeDurably()
                .withIdentity(jobName, jobGroup)
                .usingJobData("count", 1)
                .build();

        // 一个 trigger 对应一个 jobDetail
        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(triggerName, triggerGroup)
                .withSchedule(cronScheduleBuilder)
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }

    public static void pauseJob(Scheduler scheduler, String jobName) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobName);
        scheduler.pauseJob(jobKey);
    }

    public static void pauseJob(Scheduler scheduler, String jobName, String jobGroup) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        scheduler.pauseJob(jobKey);
    }

    public static void resumeJob(Scheduler scheduler, String jobName) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobName);
        scheduler.resumeJob(jobKey);
    }

    public static void resumeJob(Scheduler scheduler, String jobName, String jobGroup) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        scheduler.resumeJob(jobKey);
    }

    public static void deleteJob(Scheduler scheduler, String jobName) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobName);
        scheduler.deleteJob(jobKey);
    }

    public static void deleteJob(Scheduler scheduler, String jobName, String jobGroup) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        scheduler.deleteJob(jobKey);
    }

    public static void runJobOnce(Scheduler scheduler, String jobName) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobName);
        scheduler.triggerJob(jobKey);
    }

    public static void runJobOnce(Scheduler scheduler, String jobName, String jobGroup) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        scheduler.triggerJob(jobKey);
    }

    /**
     * Use the trigger name and group in QuartzBean to identify the trigger which needs to be modified.
     *
     * @param scheduler  scheduler
     * @param quartzBean QuartzBean
     * @throws SchedulerException
     */
    public static void modifyJob(Scheduler scheduler, QuartzBean quartzBean) throws SchedulerException {
        String triggerGroup = quartzBean.getTriggerGroup();
        TriggerKey triggerKey = TriggerKey.triggerKey(determineTriggerName(quartzBean), triggerGroup);
        CronTrigger oldTrigger = (CronTrigger)scheduler.getTrigger(triggerKey);
        // if(oldTrigger instanceof CronTrigger) {
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(quartzBean.getCronExpression());

            CronTrigger newTrigger = oldTrigger.getTriggerBuilder()
                    .withSchedule(cronScheduleBuilder)
                    .build();
            scheduler.rescheduleJob(triggerKey, newTrigger);
        // } else {
        //     throw new RuntimeException("trigger is not cron trigger");
        // }
    }

    /**
     * If the trigger name is not defined in quartzBean, trigger name will be "${jobName}_trigger"
     * @param quartzBean
     * @return
     */
    private static String determineTriggerName(QuartzBean quartzBean) {
        Objects.requireNonNull(quartzBean);
        Objects.requireNonNull(quartzBean.getJobName());

        String triggerName = quartzBean.getTriggerName();
        if (triggerName != null && !triggerName.isEmpty()) {
            return triggerName;
        }
        return quartzBean.getJobName() + "_trigger";
    }
}
