/*
 * Winner
 * 文件名  :ElkKafkaApplication.java
 * 创建人  :llxiao
 * 创建时间:2018年8月9日
 */

package com.xiao.springcloud.demo.kafka.elk;

import com.xiao.springcloud.demo.kafka.elk.kafka.KafkaProducerTest;
import com.xiao.springcloud.demo.kafka.elk.kafka.LogCompent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Optional;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年8月9日
 * @since JDK 1.8
 */
@SpringBootApplication
// 自动注册发现
@EnableDiscoveryClient
// fegin客户端
@EnableFeignClients
@EnableEurekaClient
@EnableScheduling
@Slf4j
public class ElkKafkaApplication
{
    @Autowired
    KafkaProducerTest producerTest;

    @Autowired
    LogCompent logCompent;

    public static void main(String[] args)
    {
        SpringApplication.run(ElkKafkaApplication.class, args);
    }

    // 每隔1分钟执行一次记录日志
    @Scheduled(fixedRate = 1000 * 60)
    public void testKafka()
    {
        producerTest.sendTest();
        logCompent.testLog();
    }

    // kafka的消息监听
    @KafkaListener(topics = "kafkaTest", id = "member-service")
    public void listen(ConsumerRecord<?, ?> record) throws Exception
    {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent())
        {
            Object message = kafkaMessage.get();
            log.info("----------------- record =" + record);
            log.info("------------------ re-message =" + message);
        }
    }
}
