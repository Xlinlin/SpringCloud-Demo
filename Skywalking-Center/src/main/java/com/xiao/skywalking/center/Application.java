/*
 * Winner 
 * 文件名  :Application.java
 * 创建人  :llxiao
 * 创建时间:2018年3月29日
*/

package com.xiao.skywalking.center;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * [简要描述]:配置中心、注册中心合并<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年3月29日
 * @since 
 */
@SpringBootApplication
// 注册中心
@EnableEurekaServer
// 配置中心
@EnableConfigServer
@EnableDiscoveryClient
public class Application
{
    public static void main(String[] args)
    {
        SpringApplication.run(Application.class, args);
    }
}
