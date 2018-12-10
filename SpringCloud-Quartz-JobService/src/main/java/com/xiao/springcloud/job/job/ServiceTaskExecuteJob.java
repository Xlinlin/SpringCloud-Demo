package com.xiao.springcloud.job.job;

import com.xiao.springcloud.job.entity.TaskConfigDocument;
import com.xiao.springcloud.job.quartz.JobManager;
import org.quartz.*;

/**
 * [简要描述]: 具体任务实现
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/12/10 11:53
 * @since JDK 1.8
 */
public class ServiceTaskExecuteJob implements Job
{
    /**
     * <p>
     * Called by the <code>{@link Scheduler}</code> when a <code>{@link Trigger}</code>
     * fires that is associated with the <code>Job</code>.
     * </p>
     *
     * <p>
     * The implementation may wish to set a
     * {@link JobExecutionContext#setResult(Object) result} object on the
     * {@link JobExecutionContext} before this method exits.  The result itself
     * is meaningless to Quartz, but may be informative to
     * <code>{@link JobListener}s</code> or
     * <code>{@link TriggerListener}s</code> that are watching the job's
     * execution.
     * </p>
     *
     * @param context
     * @exception JobExecutionException if there is an exception while executing the job.
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException
    {
        TaskConfigDocument task = (TaskConfigDocument) context.getMergedJobDataMap().get(JobManager.JOB_KEY);
        // 具体业务逻辑
    }
}
