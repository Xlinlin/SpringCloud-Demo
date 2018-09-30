/*
 * Winner
 * 文件名  :SkywalkingController.java
 * 创建人  :llxiao
 * 创建时间:2018年3月30日
 */

package com.xiao.skywalking.provider.controller;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年3月30日
 */
@RestController
public class SkywalkingController
{

    @RequestMapping(path = "/skywalking")
    // cacheable注解，一般建议放在service层
    @Cacheable(value = "skywalking", key = "'skywalking'.concat({#hello})", sync = true)
    public String skywalking(@RequestParam("hello") String hello)
    {
        return hello;
    }
}
