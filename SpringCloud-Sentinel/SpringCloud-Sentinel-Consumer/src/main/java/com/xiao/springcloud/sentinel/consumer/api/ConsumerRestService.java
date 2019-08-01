package com.xiao.springcloud.sentinel.consumer.api;

import com.xiao.springcloud.sentinel.consumer.feign.ProducerFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/8/1 10:29
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/consumer")
public class ConsumerRestService
{
    @Autowired
    private ProducerFeign producerFeign;

    @RequestMapping("/timeout")
    public String timeout(String input)
    {
        return producerFeign.timeout(input);
    }

    @RequestMapping("/normal")
    public String normal(String input)
    {
        return producerFeign.normal(input);
    }
}
