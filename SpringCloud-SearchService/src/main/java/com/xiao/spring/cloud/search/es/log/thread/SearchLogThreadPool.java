/*
 * Winner 
 * 文件名  :SearchLogThreadPool.java
 * 创建人  :llxiao
 * 创建时间:2018年2月24日
*/

package com.xiao.spring.cloud.search.es.log.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * [简要描述]:定制线程池<br/>
 * [详细描述]:通过扩展beforeExcute、afterExcute、terminated方法添加线程池的统计和监控功能<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年2月24日
 * @since Purcotton-Search B01
 */
public class SearchLogThreadPool extends ThreadPoolExecutor
{

    /**
     * 日志记录器
     */
    private static final Log LOG = LogFactory.getLog(SearchLogThreadPool.class);

    /**
     * 每个线程存储一个开始时间
     */
    private final ThreadLocal<Long> startTime = new ThreadLocal<>();

    /**
     * 已处理任务数量
     */
    private final AtomicLong numTasks = new AtomicLong();

    /**
     * 总处理时间
     */
    private final AtomicLong totalTime = new AtomicLong();

    /**
     * [简要描述]:<br/>
     * [详细描述]:<br/>
     *
     * @author llxiao
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param unit
     * @param workQueue
     */
    public SearchLogThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
            BlockingQueue<Runnable> workQueue)
    {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    /**
     * [简要描述]:<br/>
     * [详细描述]:<br/>
     *
     * @author llxiao
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param unit
     * @param workQueue
     * @param threadFactory
     */
    public SearchLogThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
            BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory)
    {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    /**
     * [简要描述]:<br/>
     * [详细描述]:<br/>
     *
     * @author llxiao
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param unit
     * @param workQueue
     * @param handler
     */
    public SearchLogThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
            BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler)
    {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    /**
     * [简要描述]:<br/>
     * [详细描述]:<br/>
     *
     * @author llxiao
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param unit
     * @param workQueue
     * @param threadFactory
     * @param handler
     */
    public SearchLogThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
            BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler)
    {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    /**
     * [简要描述]:线程执行之前调用方法<br/>
     * [详细描述]:出现RuntimeException时不会调用此方法<br/>
     * 
     * @author llxiao
     * @param t
     * @param r
     * @see ThreadPoolExecutor#beforeExecute(Thread,
     *      Runnable)
     */
    @Override
    protected void beforeExecute(Thread t, Runnable r)
    {
        super.beforeExecute(t, r);
        if (LOG.isDebugEnabled())
        {
            LOG.debug(t.getName() + " starting...");
        }
        startTime.set(System.nanoTime());
    }

    /**
     * [简要描述]:线程执行之后调用方法<br/>
     * [详细描述]:run正常运行完成或抛出异常会调用该方法，但出现ERROR不会执行此方法<br/>
     *
     * @author llxiao
     * @param r
     * @param t
     * @see ThreadPoolExecutor#afterExecute(Runnable,
     *      Throwable)
     */
    @Override
    protected void afterExecute(Runnable r, Throwable t)
    {
        try
        {
            long endTime = System.nanoTime();
            long taskTime = endTime - startTime.get();
            totalTime.addAndGet(taskTime);
            numTasks.incrementAndGet();
            if (LOG.isDebugEnabled())
            {
                LOG.debug("Thread " + r + " end and cost time:" + taskTime);
                LOG.debug("Throwable:" + t);
            }
        }
        finally
        {
            super.afterExecute(r, t);
        }
    }

    /**
     * [简要描述]:线程池完成关闭时调用方法<br/>
     * [详细描述]:可用于统计、关闭资源、完成发送通知等，计算线程平均执行时间<br/>
     *
     * @author llxiao
     * @see ThreadPoolExecutor#terminated()
     */
    @Override
    protected void terminated()
    {
        try
        {
            LOG.info("Thread terminated: avg time=" + (totalTime.get() / numTasks.get()));
        }
        finally
        {
            super.terminated();
        }
    }
}
