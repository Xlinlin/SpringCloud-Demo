package com.xiao.custom.config.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * [简要描述]: 注册中心、配置中心一体化
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/11/21 11:37
 * @since JDK 1.8
 */
@SpringBootApplication(scanBasePackages = "com.xiao.custom.config")
@MapperScan("com.xiao.custom.config.pojo.mapper")
@EnableEurekaServer
public class ConfigServiceApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(ConfigServiceApplication.class, args);
    }

    // 启动的时候要注意，由于我们在controller中注入了RestTemplate，所以启动的时候需要实例化该类的一个实例
    @Autowired
    private RestTemplateBuilder builder;

    // 使用RestTemplateBuilder来实例化RestTemplate对象，spring默认已经注入了RestTemplateBuilder实例
    @Bean
    public RestTemplate restTemplate()
    {
        return builder.build();
    }
}
