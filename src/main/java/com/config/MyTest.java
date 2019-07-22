package com.config;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/*
 * 定时任务Job
 */
public class MyTest implements Job{

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
               System.out.println("test");
    }
}
