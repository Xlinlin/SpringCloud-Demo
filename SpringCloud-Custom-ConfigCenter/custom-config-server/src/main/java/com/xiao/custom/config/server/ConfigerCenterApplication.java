package com.xiao.custom.config.server;

import com.xiao.custom.config.server.annotation.CustomEnableConfigServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * [简要描述]: 自定义配置中心实现,注册中心、配置中心合并一体化
 * [详细描述]: spring-cloud config扩展
 *
 * @author llxiao
 * @version 1.0, 2019/1/31 10:01
 * @since JDK 1.8
 */
@SpringBootApplication(scanBasePackages = "com.xiao.custom.config")
@CustomEnableConfigServer
@EnableEurekaServer
public class ConfigerCenterApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(ConfigerCenterApplication.class, args);
    }
}
