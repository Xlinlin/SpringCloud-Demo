package com.xiao.springcloud.demo.common.forkjoin;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ForkJoinPool;

/**
 * [简要描述]: fork join 配置
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/5/14 16:14
 * @since JDK 1.8
 */
@Configuration
public class ForkjoinConfiguration implements DisposableBean
{
    private ForkJoinPool forkJoinPool;

    @Bean
    public ForkJoinPool forkJoinPool()
    {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        this.forkJoinPool = forkJoinPool;
        return forkJoinPool;
    }

    /**
     * Invoked by a BeanFactory on destruction of a singleton.
     *
     * @exception Exception in case of shutdown errors.
     * Exceptions will get logged but not rethrown to allow
     * other beans to release their resources too.
     */
    @Override
    public void destroy() throws Exception
    {
        if (null != forkJoinPool)
        {
            forkJoinPool.shutdown();
        }
    }
}
