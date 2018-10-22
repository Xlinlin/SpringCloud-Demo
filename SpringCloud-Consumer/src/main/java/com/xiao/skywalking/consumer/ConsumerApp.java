/*
 * Winner
 * 文件名  :ConsumerApp.java
 * 创建人  :llxiao
 * 创建时间:2018年3月30日
 */

package com.xiao.skywalking.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年3月30日
 */
@SpringBootApplication
// 自动注册发现
@EnableDiscoveryClient
// fegin客户端
@EnableFeignClients
// 开启熔断功能
@EnableCircuitBreaker
public class ConsumerApp
{
    /**
     * 实例化RestTemplate使用@LoadBalanced开启负载均衡
     * [简要描述]:<br/>
     * [详细描述]:<br/>
     *
     * @return
     */
    @Bean
    @LoadBalanced
    public RestTemplate resTemplate()
    {
        return new RestTemplate();
    }

    public static void main(String[] args)
    {
        SpringApplication.run(ConsumerApp.class, args);
    }
}
