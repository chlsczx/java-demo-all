package com.czx.demo.springboot.quartz.controller;

import com.czx.demo.springboot.quartz.model.QuartzBean;
import com.czx.demo.springboot.quartz.model.builder.QuartzBeanBuilder;
import com.czx.demo.springboot.quartz.quarz.job.MyJob;
import com.czx.demo.springboot.quartz.utils.JobUtils;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/quartz")
public class QuartzController {

    private static final Logger log = LoggerFactory.getLogger(QuartzController.class);
    String jobName = "myJob";

    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    Scheduler scheduler;

    public QuartzController(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String createJob() throws SchedulerException {
        QuartzBean myJob = QuartzBeanBuilder.newQuartzBean()
                .jobName(jobName + atomicInteger.incrementAndGet())
                .jobClass(MyJob.class)
                .cronExpression("0/5 * * * * ? *")
                .build();
        JobUtils.createJob(scheduler, myJob);
        return "create job";
    }

    @RequestMapping(value = "/pause", method = RequestMethod.GET)
    public String pauseJob() throws SchedulerException {
        JobUtils.pauseJob(scheduler, jobName);
        return "pause job";
    }

    @RequestMapping(value = "/resume", method = RequestMethod.GET)
    public String resumeJob() throws SchedulerException {
        JobUtils.resumeJob(scheduler, jobName);
        return "resume job";
    }

    @RequestMapping(value = "/runOnce", method = RequestMethod.GET)
    public String runJobOnce() throws SchedulerException {
        JobUtils.runJobOnce(scheduler, jobName);
        return "runOnce job";
    }

    @RequestMapping(value = "/modify/{id}", method = RequestMethod.GET)
    public String modifyJob(@PathVariable("id") int id) throws SchedulerException {
        log.info("modify job id:{}", id);
        QuartzBean myJob = QuartzBeanBuilder.newQuartzBean()
                .jobName(jobName+id)
                .jobClass(MyJob.class)
                .cronExpression("0/1 * * * * ? *")
                .build();
        JobUtils.modifyJob(scheduler, myJob);
        return "modify job";
    }
}
