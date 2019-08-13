package com.xiao.hystrix.demo.consumer.feign.impl;

import com.xiao.hystrix.demo.consumer.feign.ProducerFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.RejectedExecutionException;

/**
 * [简要描述]: 工厂模式实现fallback
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/8/6 09:51
 * @since JDK 1.8
 */
@Component
public class ProducerFeignFactory implements FallbackFactory<ProducerFeign>
{
    private final ProducerFeignFallBack producerFeignFallBack;

    public ProducerFeignFactory(ProducerFeignFallBack producerFeignFallBack)
    {
        this.producerFeignFallBack = producerFeignFallBack;
    }

    /**
     * Returns an instance of the fallback appropriate for the given cause
     */
    @Override
    public ProducerFeign create(Throwable cause)
    {
        if (cause instanceof RejectedExecutionException)
        {
            cause.printStackTrace();
        }
        return producerFeignFallBack;
    }
}
