/*
 * Winner
 * 文件名  :FeignService.java
 * 创建人  :llxiao
 * 创建时间:2018年3月30日
 */

package com.xiao.skywalking.consumer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.xiao.skywalking.consumer.feign.impl.FeignServiceImpl;

/**
 * [简要描述]:feign客户端，以及fallback熔断<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年3月30日
 */
@FeignClient(value = "provider-1112", fallback = FeignServiceImpl.class)
public interface FeignService
{
    @RequestMapping(value = "/skywalking")
    String helloSkywalking(@RequestParam(value = "hello") String hello);
}
