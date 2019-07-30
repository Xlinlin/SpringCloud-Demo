package com.xiao.spring.boot.admin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

/**
 * [简要描述]: springboot-admin 集成eureka 监控服务
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/7/30 09:47
 * @since JDK 1.8
 */
@Configuration
@SpringBootApplication
@EnableAdminServer
public class SpringBootAdminApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(SpringBootAdminApplication.class, args);
    }
}
