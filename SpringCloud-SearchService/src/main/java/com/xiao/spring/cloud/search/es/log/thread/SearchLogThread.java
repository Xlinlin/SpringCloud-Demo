/*
 * Winner 
 * 文件名  :SearchLogThread.java
 * 创建人  :llxiao
 * 创建时间:2018年2月24日
*/

package com.xiao.spring.cloud.search.es.log.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * [简要描述]:定制线程，指定线程名称，设置未捕获异常，维护一些统计信息<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年2月24日
 * @since Purcotton-Search B01
 */
public class SearchLogThread extends Thread
{
    private static final Log LOG = LogFactory.getLog(SearchLogThread.class);

    /**
     * 创建线程数
     */
    private static final AtomicInteger CREATED = new AtomicInteger();

    /**
     * 运行线程数
     */
    private static final AtomicInteger ALIVE = new AtomicInteger();

    public SearchLogThread(Runnable r, String threadName)
    {
        super(r, threadName + '-' + CREATED.incrementAndGet());
        setUncaughtExceptionHandler((t, e) -> LOG.error(t.getName() + " UncaughtExceptionHandler", e));
    }

    @Override
    public void run()
    {
        if (LOG.isDebugEnabled())
        {
            LOG.debug("Create thread:" + getName());
        }
        try
        {
            ALIVE.incrementAndGet();
            super.run();
        }
        finally
        {
            ALIVE.decrementAndGet();
            if (LOG.isDebugEnabled())
            {
                LOG.debug("Exit thread:" + getName());
            }
        }
    }

    /**
     * 返回created属性
     * 
     * @return created属性
     */
    public static AtomicInteger getCreated()
    {
        return CREATED;
    }

    /**
     * 返回alive属性
     * 
     * @return alive属性
     */
    public static AtomicInteger getAlive()
    {
        return ALIVE;
    }

}
