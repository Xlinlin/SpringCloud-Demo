package com.xiao.spring.cloud.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * [简要描述]:主启动类
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/10/8 15:07
 * @since JDK 1.8
 */
@SpringBootApplication
@EnableEurekaClient
public class SearchApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(SearchApplication.class, args);
    }
}
