package com.config;

import org.quartz.*;
import org.quartz.ee.servlet.QuartzInitializerListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;

@Configuration
public class QuartzConfig {

    @Bean(name = "SchedulerFactory")
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        //用于quartz集群,QuartzScheduler 启动时更新己存在的Job，这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了
        factory.setOverwriteExistingJobs(true);
        //任务调度监听类
        //factory.setGlobalTriggerListeners(triggerListenerLogMonitor());
        return factory;
    }

    /*
    * quartz初始化监听器
    */
    @Bean
    public QuartzInitializerListener executorListener() {
        return new QuartzInitializerListener();
    }

    /*
    *任务调度监听类
    */
    @Bean
    public TriggerListenerLogMonitor triggerListenerLogMonitor() {
        return new TriggerListenerLogMonitor();
    }

    /*
  * 通过SchedulerFactoryBean获取Scheduler的实例
  */
    @Bean(name = "Scheduler")
    public Scheduler scheduler() throws IOException {
        Scheduler scheduler = schedulerFactoryBean().getScheduler();
        //添加同步任务
        addmyTestJob(scheduler);
        return scheduler;
    }

    /**
     * 新增一个定时任务（测试）
     * @param scheduler
     * @author sujin
     */
    private void addmyTestJob(Scheduler scheduler){
        String startJob = "true";//是否开始
        String jobName = "DATAMART_SYNC";
        String jobGroup = "DATAMART_SYNC";
        String cron = "0/5 * * * * ?";//定时的时间设置:5秒运行1次
        String className = MyTest.class.getName();
        if (startJob != null && startJob.equals("true")) {
            addCommonCronJob(jobName, jobGroup, cron, scheduler, className);
        } else {
            deleteCommonJob(jobName, jobGroup, scheduler);
        }
    }
    //end...

    private void deleteCommonJob(String jobName, String jobGroup, Scheduler scheduler) {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        try {
            scheduler.pauseJob(jobKey);//先暂停任务
            scheduler.deleteJob(jobKey);//再删除任务
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加一些定时任务，如日志清理任务
     */
    private void addCommonCronJob(String jobName, String jobGroup, String cron, Scheduler scheduler, String className) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
            //任务触发
            Trigger checkExist = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (checkExist == null) {
                JobDetail jobDetail = null;
                jobDetail = JobBuilder.newJob((Class<? extends Job>) Class.forName(className))
                        .requestRecovery(true)//当Quartz服务被中止后，再次启动或集群中其他机器接手任务时会尝试恢复执行之前未完成的所有任务
                        .withIdentity(jobName, jobGroup)
                        .build();
                jobDetail.getJobDataMap().put("jobName", jobName);
                jobDetail.getJobDataMap().put("jobGroup", jobGroup);
                CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
                /*withMisfireHandlingInstructionDoNothing
                ——不触发立即执行
                ——等待下次Cron触发频率到达时刻开始按照Cron频率依次执行
	                withMisfireHandlingInstructionIgnoreMisfires
	                ——以错过的第一个频率时间立刻开始执行
	                ——重做错过的所有频率周期后
	                ——当下一次触发频率发生时间大于当前时间后，再按照正常的Cron频率依次执行
	                withMisfireHandlingInstructionFireAndProceed
	                ——以当前时间为触发频率立刻触发一次执行
	                ——然后按照Cron频率依次执行*/
                Trigger trigger = TriggerBuilder.newTrigger()
                        .withIdentity(jobName, jobGroup)
                        .withSchedule(cronScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires())
                        .build();
                scheduler.scheduleJob(jobDetail, trigger);
            } else {
                scheduler.deleteJob(JobKey.jobKey(jobName, jobGroup));
                addCommonCronJob(jobName, jobGroup, cron, scheduler, className);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
