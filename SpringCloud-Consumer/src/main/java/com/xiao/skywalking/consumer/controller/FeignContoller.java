/*
 * Winner 
 * 文件名  :FeignContoller.java
 * 创建人  :llxiao
 * 创建时间:2018年3月30日
*/

package com.xiao.skywalking.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiao.skywalking.consumer.feign.FeignService;
import com.xiao.skywalking.consumer.ribbon.RibbonService;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年3月30日
 */
@RestController
@RefreshScope
public class FeignContoller
{
    @Autowired
    FeignService feignService;

    @Autowired
    RibbonService ribbonService;

    // @Value("${profile}")
    // private String profile;

    @RequestMapping(path = "/feign")
    public String feign(String hello)
    {
        // return feignService.helloSkywalking(hello) + ' ' + this.profile;
        return feignService.helloSkywalking(hello);
    }

    @RequestMapping(path = "/ribbon")
    public String ribbon(String hello)
    {
        return ribbonService.helloSkywalking(hello);
    }
}
