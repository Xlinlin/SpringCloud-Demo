/*
 * Winner
 * 文件名  :ZipkinServerApplication.java
 * 创建人  :llxiao
 * 创建时间:2018年8月9日
 */

package com.xiao.springcloud.zs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zipkin.server.EnableZipkinServer;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年8月9日
 * @since JDK 1.8
 */
@SpringBootApplication
// 关联上配置中心
//@EnableEurekaClient
// 加上注解@EnableZipkinServer，开启ZipkinServer的功能
@EnableZipkinServer
//@EnableZipkinStreamServer
public class ZipkinServerApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(ZipkinServerApplication.class, args);
    }
}
