package com.xiao.hystrix.demo.consumer.feign.impl;

import com.xiao.hystrix.demo.consumer.feign.ProducerFeign;
import org.springframework.stereotype.Component;

/**
 * [简要描述]: fallback
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/8/1 10:31
 * @since JDK 1.8
 */
@Component
public class ProducerFeignFallBack implements ProducerFeign
{
    @Override
    public String timeout(String input)
    {
        return "Time out fall back!  " + input;
    }

    @Override
    public String normal(String input)
    {
        return "Normal fall back!  " + input;
    }
}
