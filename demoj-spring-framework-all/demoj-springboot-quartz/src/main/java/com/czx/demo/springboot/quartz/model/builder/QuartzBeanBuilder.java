package com.czx.demo.springboot.quartz.model.builder;


import com.czx.demo.springboot.quartz.model.QuartzBean;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.quartz.Job;

public class QuartzBeanBuilder {

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();

    private QuartzBean quartzBean;

    public static QuartzBeanBuilder newQuartzBean() {
        return new QuartzBeanBuilder(new QuartzBean());
    }

    /**
     * Return the built quartBean, and refresh the storing reference to a new one.
     * @return this built quartBean
     */
    @Valid
    public QuartzBean build() {
        QuartzBean result = this.quartzBean;
        validator.validate(result);
        this.quartzBean = new QuartzBean();
        return result;
    }

    public QuartzBeanBuilder jobName(String jobName) {
        this.quartzBean.setJobName(jobName);
        return this;
    }

    public QuartzBeanBuilder jobGroup(String jobGroup) {
        this.quartzBean.setJobGroup(jobGroup);
        return this;
    }

    public QuartzBeanBuilder jobClass(Class<? extends Job> jobClass) {
        this.quartzBean.setJobClass(jobClass);
        return this;
    }

    public QuartzBeanBuilder triggerName(String triggerName) {
        this.quartzBean.setTriggerName(triggerName);
        return this;
    }

    public QuartzBeanBuilder triggerGroup(String triggerGroup) {
        this.quartzBean.setTriggerGroup(triggerGroup);
        return this;
    }

    public QuartzBeanBuilder cronExpression(String cronExpression) {
        this.quartzBean.setCronExpression(cronExpression);
        return this;
    }

    public QuartzBeanBuilder(QuartzBean quartzBean) {
        this.quartzBean = quartzBean;
    }
}
