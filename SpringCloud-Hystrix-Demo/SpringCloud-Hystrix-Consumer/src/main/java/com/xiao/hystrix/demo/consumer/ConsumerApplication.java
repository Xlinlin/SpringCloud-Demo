package com.xiao.hystrix.demo.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

//import org.springframework.cloud.alibaba.sentinel.annotation.SentinelRestTemplate;
//import org.springframework.context.annotation.Bean;
//import org.springframework.web.client.RestTemplate;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/8/1 10:27
 * @since JDK 1.8
 */
@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
public class ConsumerApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(ConsumerApplication.class, args);
    }

    @Bean
    //    @SentinelRestTemplate
    //    @LoadBalanced
    public RestTemplate restTemplate()
    {
        return new RestTemplate();
    }
}
