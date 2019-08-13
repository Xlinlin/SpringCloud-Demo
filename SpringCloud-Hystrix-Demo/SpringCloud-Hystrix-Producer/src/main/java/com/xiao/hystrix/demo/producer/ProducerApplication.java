package com.xiao.hystrix.demo.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/8/1 09:46
 * @since JDK 1.8
 */
@SpringBootApplication
@EnableEurekaClient
public class ProducerApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(ProducerApplication.class, args);
    }

    //    @Bean
    //    @SentinelRestTemplate
    //    public RestTemplate restTemplate()
    //    {
    //        return new RestTemplate();
    //    }
}
