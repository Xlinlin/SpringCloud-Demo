package com.xiao.springcloud.sentinel.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/8/1 09:40
 * @since JDK 1.8
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(EurekaApplication.class, args);
    }
}
