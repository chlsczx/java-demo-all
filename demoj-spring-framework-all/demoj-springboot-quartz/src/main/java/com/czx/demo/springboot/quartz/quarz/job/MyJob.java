package com.czx.demo.springboot.quartz.quarz.job;

import org.quartz.*;

import java.util.Date;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class MyJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // -> context

        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        System.out.println("========================================================");
        // 输出名字
        System.out.println("Job name: " + jobDetail.getKey().getName());
        // 输出 group
        System.out.println("Job group: " + jobDetail.getKey().getGroup());
        // 输出 class
        System.out.println("Job class: " + jobDetail.getJobClass().getName());
        // 输出执行时间
        System.out.println("Execution time: " + jobExecutionContext.getFireTime());
        // 输出下次执行时间
        System.out.println("Next execution time: " + jobExecutionContext.getNextFireTime());

        // System.out.println("System.identityHashCode(jobDetail) = " +System.identityHashCode(jobDetail));

        // Job：任务执行次数
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        Integer count = (Integer) jobDataMap.get("count");
        System.out.printf("count: %d\n", count);


        jobDataMap.put("count", count + 1);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("=======================\ncount " + count + " task end at "+ new Date()+" !\n------------------------------" );
    }
}
