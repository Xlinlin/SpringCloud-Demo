package com.xiao.hystrix.demo.consumer.feign;

import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import com.xiao.hystrix.demo.consumer.common.CacheConstants;
import com.xiao.hystrix.demo.consumer.feign.impl.ProducerFeignFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @since JDK 1.8
 */
@FeignClient(name = "pref-producer-service", fallbackFactory = ProducerFeignFactory.class, path = "/api")
public interface ProducerFeign
{
    @CacheResult
    @RequestMapping("/timeout")
    @Cacheable(value = CacheConstants.CFG_DATA_CACHE)
    String timeout(@RequestParam("input") String input);

    @CacheResult
    @RequestMapping("/normal")
    @Cacheable(value = CacheConstants.CFG_DATA_CACHE)
    String normal(@RequestParam("input") String input);
}