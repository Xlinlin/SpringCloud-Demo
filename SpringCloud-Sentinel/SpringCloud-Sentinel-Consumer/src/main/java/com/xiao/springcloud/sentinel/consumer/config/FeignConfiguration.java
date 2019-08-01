package com.xiao.springcloud.sentinel.consumer.config;

import com.xiao.springcloud.sentinel.consumer.feign.fallback.ProducerFeignFallBack;
import org.springframework.context.annotation.Bean;

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
    @Bean
    public ProducerFeignFallBack feignFallBack()
    {
        return new ProducerFeignFallBack();
    }
}
