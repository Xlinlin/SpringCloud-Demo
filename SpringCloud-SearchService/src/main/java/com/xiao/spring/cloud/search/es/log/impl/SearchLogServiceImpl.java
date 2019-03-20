/*
 * Winner
 * 文件名  :SearchLogService.java
 * 创建人  :llxiao
 * 创建时间:2018年2月23日
 */

package com.xiao.spring.cloud.search.es.log.impl;

import com.xiao.spring.cloud.search.dto.SearchLogDo;
import com.xiao.spring.cloud.search.es.log.ISearchLogService;
import com.xiao.spring.cloud.search.es.log.thread.BatchSaveSearchLogThread;
import com.xiao.spring.cloud.search.es.log.thread.SearchThreadFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * [简要描述]:搜索日志服务实现类<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年2月23日
 * @since Purcotton-Search B01
 */
@Service
public class SearchLogServiceImpl implements ISearchLogService
{
    /**
     * 日志记录
     */
    private static final Log LOG = LogFactory.getLog(SearchLogServiceImpl.class);

    /**
     * 最大1千个有界队列
     */
    private final ArrayBlockingQueue<SearchLogDo> queue = new ArrayBlockingQueue<>(1000);

    /**
     * 批处理数量
     */
    private static final int BATCH_SIZE = 20;

    /**
     * 定时执行任务
     */
    private ScheduledExecutorService schedule = new ScheduledThreadPoolExecutor(5, new SearchThreadFactory());

    @PostConstruct
    public void init()
    {
        // 这个是按照固定的时间来执行，简单来说：到点执行
        // initialDelay延迟时间 period周期时间 每个60秒 延迟10秒执行一个线程
        schedule.scheduleAtFixedRate(new BatchSaveSearchLogThread(queue, BATCH_SIZE), 10, 60, TimeUnit.SECONDS);
        // scheduleWithFixedDelay，执行完上一个任务后再执行
    }

    /**
     * [简要描述]:添加一个搜索日志<br/>
     * [详细描述]:<br/>
     *
     * @param searchLog
     * @see
     */
    @Override
    public void addSearchLog(SearchLogDo searchLog)
    {
        if (!queue.offer(searchLog))
        {
            // 饱和策略
            LOG.warn("日志队列已满...................");
        }
    }

    /**
     * [简要描述]:销毁线程池<br/>
     * [详细描述]:<br/>
     */
    @PreDestroy
    public void destroy()
    {
        schedule.shutdown();
    }
}
