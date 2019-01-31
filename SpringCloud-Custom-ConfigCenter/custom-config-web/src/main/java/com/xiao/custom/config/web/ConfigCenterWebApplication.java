package com.xiao.custom.config.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/11/27 09:00
 * @since JDK 1.8
 */
@SpringBootApplication(scanBasePackages = "com.xiao.custom.config")
@EnableEurekaClient
//开启fegin
@EnableFeignClients
// 开启熔断功能
//@EnableCircuitBreaker
public class ConfigCenterWebApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(ConfigCenterWebApplication.class, args);
    }
}
