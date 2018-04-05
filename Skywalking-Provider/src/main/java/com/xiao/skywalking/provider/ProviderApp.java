/*
 * Winner 
 * 文件名  :ProviderApp.java
 * 创建人  :llxiao
 * 创建时间:2018年3月30日
*/

package com.xiao.skywalking.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年3月30日
 */
@SpringBootApplication
// @EnableEurekaClient 表明自己是一个eurekaclient
@EnableEurekaClient
public class ProviderApp
{
    public static void main(String[] args)
    {
        SpringApplication.run(ProviderApp.class, args);
    }
}
