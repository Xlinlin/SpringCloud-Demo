package com.xiao.springcloud.job.quartz;

import com.xiao.springcloud.job.entity.TaskConfigDocument;
import com.xiao.springcloud.job.job.ServiceTaskExecuteJob;
import com.xiao.springcloud.job.util.CronExpUtil;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

/**
 * [简要描述]:任务管理<br/>
 * [详细描述]:<br/>
 * llxiao  2018/12/10 - 13:47
 **/
@Service
public class JobManager implements InitializingBean
{

    public static final String JOB_KEY = "SCHEDULED_JOB";

    private static Logger logger = LoggerFactory.getLogger(JobManager.class);

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    /**
     * 添加任务
     *
     * @param task
     */
    public void addJob(TaskConfigDocument task)
    {
        boolean cronExp = CronExpUtil.validator(task.getCronExp());
        if (cronExp)
        {
            try
            {
                Scheduler scheduler = schedulerFactoryBean.getScheduler();
                logger.info("add job " + task.getName());
                TriggerKey triggerKey = TriggerKey.triggerKey(task.getName(), task.getModule());
                CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

                if (trigger == null)
                {
                    JobDetail jobDetail = JobBuilder.newJob(ServiceTaskExecuteJob.class)
                            .withIdentity(task.getName(), task.getModule()).build();
                    jobDetail.getJobDataMap().put(JOB_KEY, task);
                    //表达式调度构建器
                    CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(task.getCronExp());
                    //按新的cronExpression表达式构建一个新的trigger
                    trigger = TriggerBuilder.newTrigger().withIdentity(task.getName(), task.getModule())
                            .withSchedule(scheduleBuilder).build();
                    scheduler.scheduleJob(jobDetail, trigger);
                }
                else
                {
                    //if (!trigger.getCronExpression().equals(task.getCronExp())) {
                    // Trigger已存在，那么更新相应的定时设置
                    CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(task.getCronExp());
                    // 按新的cronExpression表达式重新构建trigger
                    trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder)
                            .build();
                    // 按新的trigger重新设置job执行
                    scheduler.rescheduleJob(triggerKey, trigger);

                }
                logger.info("add job " + task.getName() + " finished");

            }
            catch (Exception e)
            {
                logger.error("add job '{}' error: {}", task.getName(), e.getMessage());
                throw new RuntimeException(e);
            }
        }
        else
        {
            // 表达式非法或者已过期 直接停止or删除
            this.pauseJob(task);
            //            this.deleteJob(task);
        }

    }

    /**
     * 停止任务
     *
     * @param task
     */
    public void pauseJob(TaskConfigDocument task)
    {
        try
        {
            logger.info("stop job " + task.getName());
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobKey jobKey = JobKey.jobKey(task.getName(), task.getModule());
            scheduler.pauseJob(jobKey);
            logger.info("stop job " + task.getName() + " finished");
        }
        catch (SchedulerException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 恢复任务
     *
     * @param task
     */
    public void resumeJob(TaskConfigDocument task)
    {

        try
        {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobKey jobKey = JobKey.jobKey(task.getName(), task.getModule());
            scheduler.resumeJob(jobKey);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除任务
     *
     * @param task
     */
    public void deleteJob(TaskConfigDocument task)
    {
        try
        {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobKey jobKey = JobKey.jobKey(task.getName(), task.getModule());
            scheduler.deleteJob(jobKey);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 立即执行
     *
     * @param task
     */
    public void runNow(TaskConfigDocument task)
    {
        try
        {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobKey jobKey = JobKey.jobKey(task.getName(), task.getModule());
            scheduler.triggerJob(jobKey);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        // 系统启动读取所有待启动的，然后执行启动任务
        //读取所有的taskConfig addJob
        //        addJob(task);
    }

}
