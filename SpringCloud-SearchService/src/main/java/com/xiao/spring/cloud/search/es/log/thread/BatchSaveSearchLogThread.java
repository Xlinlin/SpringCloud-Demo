/*
 * Winner
 * 文件名  :SaveSearchLogThread.java
 * 创建人  :llxiao
 * 创建时间:2018年2月23日
 */

package com.xiao.spring.cloud.search.es.log.thread;

import com.xiao.spring.cloud.search.dto.SearchLogDo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * [简要描述]:批处理搜索日志<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年2月23日
 * @since Purcotton-Search B01
 */
public class BatchSaveSearchLogThread implements Runnable
{
    private static final Log LOG = LogFactory.getLog(BatchSaveSearchLogThread.class);
    /**
     * 搜索日志
     */
    private static final Log SEARCH_LOG = LogFactory.getLog("searchLog");

    private ArrayBlockingQueue<SearchLogDo> queue;
    private int batchSize;

    public BatchSaveSearchLogThread(ArrayBlockingQueue<SearchLogDo> queue, int batchSize)
    {
        this.queue = queue;
        this.batchSize = batchSize;
    }

    @Override
    public void run()
    {
        if (null != queue && !queue.isEmpty())
        {
            if (LOG.isInfoEnabled())
            {
                LOG.info("Batch process search log!..........");
            }

            // 队里处理日志
            processQueue();
        }
    }

    /**
     * [简要描述]:队里处理日志<br/>
     * [详细描述]:<br/>
     */
    private void processQueue()
    {
        try
        {
            List<SearchLogDo> logs = new ArrayList<>(batchSize);
            SearchLogDo log = queue.poll();
            if (queue.size() <= batchSize)
            {
                while (null != log)
                {
                    logs.add(log);
                    log = queue.poll();
                }
            }
            else
            {
                int i = 0;
                while (null != log && i < batchSize)
                {
                    logs.add(log);
                    log = queue.poll();
                    i++;
                }
            }
            if (null != SEARCH_LOG)
            {
                processLog(logs);
            }
        }
        catch (RuntimeException e)
        {
            // 线程内部捕获RuntimeException异常，记录异常日志
            LOG.error(Thread.currentThread().getName() + " RuntimeException", e);
        }
    }

    /**
     * [简要描述]:批量日志处理<br/>
     * [详细描述]:<br/>
     *
     * @param logs 批量日志
     */
    private void processLog(List<SearchLogDo> logs)
    {
        for (SearchLogDo searchLogDo : logs)
        {
            SEARCH_LOG.info(searchLogDo);
        }

    }

}
