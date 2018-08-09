/*
 * Winner 
 * 文件名  :FeignServiceImpl.java
 * 创建人  :llxiao
 * 创建时间:2018年3月30日
*/

package com.xiao.skywalking.consumer.feign.impl;

import org.springframework.stereotype.Component;

import com.xiao.skywalking.consumer.feign.FeignService;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年3月30日
 */
@Component
public class FeignServiceImpl implements FeignService
{

    @Override
    public String helloSkywalking(String hello)
    {
        return "Service error!";
    }

}
