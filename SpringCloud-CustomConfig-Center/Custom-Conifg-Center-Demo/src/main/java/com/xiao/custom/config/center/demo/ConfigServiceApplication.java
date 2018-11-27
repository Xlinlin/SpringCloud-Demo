package com.xiao.custom.config.center.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * [简要描述]: 注册中心、配置中心一体化
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/11/21 11:37
 * @since JDK 1.8
 */
@SpringBootApplication()
@EnableEurekaClient
public class ConfigServiceApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(ConfigServiceApplication.class, args);
    }
}
