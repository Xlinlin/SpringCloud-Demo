/*
 * Winner 
 * 文件名  :RibbonService.java
 * 创建人  :llxiao
 * 创建时间:2018年3月30日
*/

package com.xiao.skywalking.consumer.ribbon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年3月30日
 */
@Service
public class RibbonService
{
    @Autowired
    private RestTemplate restTemplate;

    // HystrixCommand出现异常调用fallback方法
    @HystrixCommand(fallbackMethod = "fallback")
    public String helloSkywalking(String hello)
    {
        return this.restTemplate.getForObject("http://provider-1112/skywalking?hello=" + hello, String.class);
    }

    public String fallback(String hello)
    {
        return "Service error!";
    }
}
