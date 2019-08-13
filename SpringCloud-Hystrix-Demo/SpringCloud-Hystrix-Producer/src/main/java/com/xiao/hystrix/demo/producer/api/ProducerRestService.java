package com.xiao.hystrix.demo.producer.api;

import com.xiao.hystrix.demo.producer.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/8/1 09:55
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/api")
public class ProducerRestService
{
    @Autowired
    private ProducerService producerService;

    @RequestMapping("/timeout")
    public String timeout(String input)
    {
        return this.producerService.timeout(input);
    }

    @RequestMapping("/normal")
    public String normal(String input)
    {
        return this.producerService.normal(input);
    }
}
