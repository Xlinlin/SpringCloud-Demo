package com.xiao.springcloud.sentinel.consumer.feign.fallback;

import com.xiao.springcloud.sentinel.consumer.feign.ProducerFeign;

/**
 * [简要描述]: fallback
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/8/1 10:31
 * @since JDK 1.8
 */
public class ProducerFeignFallBack implements ProducerFeign
{
    @Override
    public String timeout(String input)
    {
        return "Time out fall back!";
    }

    @Override
    public String normal(String input)
    {
        return "Normal fall back!";
    }
}
