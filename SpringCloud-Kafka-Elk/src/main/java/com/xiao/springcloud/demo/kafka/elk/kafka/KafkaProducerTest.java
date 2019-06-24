/*
 * Winner
 * 文件名  :KafkaProducerTest.java
 * 创建人  :llxiao
 * 创建时间:2018年8月7日
 */

package com.xiao.springcloud.demo.kafka.elk.kafka;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.xiao.springcloud.demo.common.logaspect.LogAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年8月7日
 * @since JDK 1.8
 */
@Component
@Slf4j
public class KafkaProducerTest
{
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 发送消息到kafka,主题为test
     */
    @LogAnnotation
    public void sendTest()
    {
        System.out.println("-=-=-=-=-=-=-=-=send message to kafka!!!!");
        kafkaTemplate.send("kafkaTest", "hello,kafka from Producer test!!!!" + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        log.info("hello,kafka from Producer test!!!!" + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
    }
}
