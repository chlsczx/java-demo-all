package com.czx.demo.springboot.quartz.model;

import com.czx.demo.springboot.quartz.model.builder.QuartzBeanBuilder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.quartz.Job;

public class QuartzBean {
    @NotBlank
    String jobName;

    String jobGroup;

    @NotNull
    Class<? extends Job> jobClass;

    String triggerName;

    String triggerGroup;

    @NotBlank
    String cronExpression;

    public QuartzBeanBuilder getQuartzBeanBuilder() {
        return new QuartzBeanBuilder(this);
    }

    public QuartzBean() {}

    public @NotBlank String getJobName() {
        return jobName;
    }

    public void setJobName(@NotBlank String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public @NotNull Class<? extends Job> getJobClass() {
        return jobClass;
    }

    public void setJobClass(@NotNull Class<? extends Job> jobClass) {
        this.jobClass = jobClass;
    }

    public @NotBlank String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(@NotBlank String triggerName) {
        this.triggerName = triggerName;
    }

    public String getTriggerGroup() {
        return triggerGroup;
    }

    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }

    public @NotBlank String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(@NotBlank String cronExpression) {
        this.cronExpression = cronExpression;
    }
}
