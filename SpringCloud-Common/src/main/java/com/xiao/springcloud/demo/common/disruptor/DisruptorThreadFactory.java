package com.xiao.springcloud.demo.common.disruptor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/3/25 14:49
 * @since JDK 1.8
 */
public class DisruptorThreadFactory implements ThreadFactory
{
    private static final AtomicLong THREAD_NUMBER = new AtomicLong(1);

    private static final ThreadGroup THREAD_GROUP = new ThreadGroup("disruptor");

    private static volatile boolean daemon;

    private final String namePrefix;

    private DisruptorThreadFactory(final String namePrefix, final boolean daemon)
    {
        this.namePrefix = namePrefix;
        DisruptorThreadFactory.daemon = daemon;
    }

    /**
     * Constructs a new {@code Thread}.  Implementations may also initialize
     * priority, name, daemon status, {@code ThreadGroup}, etc.
     *
     * @param r a runnable to be executed by new thread instance
     * @return constructed thread, or {@code null} if the request to
     * create a thread is rejected
     */
    @Override
    public Thread newThread(Runnable r)
    {
        Thread thread = new Thread(THREAD_GROUP, r,
                THREAD_GROUP.getName() + "-" + namePrefix + "-" + THREAD_NUMBER.getAndIncrement());
        thread.setDaemon(daemon);
        if (thread.getPriority() != Thread.NORM_PRIORITY)
        {
            thread.setPriority(Thread.NORM_PRIORITY);
        }
        return thread;
    }

    /**
     * 自定义线程factory
     *
     * @param namePrefix
     * @param daemon
     * @return
     */
    public static ThreadFactory create(final String namePrefix, final boolean daemon)
    {
        return new DisruptorThreadFactory(namePrefix, daemon);
    }
}
