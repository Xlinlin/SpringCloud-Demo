package com.xiao.hystrix.demo.consumer.config;

import com.xiao.hystrix.demo.consumer.feign.impl.ProducerFeignFallBack;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/8/1 16:11
 * @since JDK 1.8
 */
//@Configuration
public class FeignConfiguration
{
    //    @Bean
    public ProducerFeignFallBack feignFallBack()
    {
        return new ProducerFeignFallBack();
    }
}
