package com.xiao.springcloud.sentinel.consumer.feign;

import com.xiao.springcloud.sentinel.consumer.config.FeignConfiguration;
import com.xiao.springcloud.sentinel.consumer.feign.fallback.ProducerFeignFallBack;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/8/1 10:29
 * @since JDK 1.8
 */
@Component
@FeignClient(name = "sentinel-producer-service", fallback = ProducerFeignFallBack.class, configuration = FeignConfiguration.class)
@RequestMapping("/api")
public interface ProducerFeign
{
    @RequestMapping("/timeout")
    String timeout(@RequestParam("input") String input);

    @RequestMapping("/normal")
    String normal(@RequestParam("input") String input);
}