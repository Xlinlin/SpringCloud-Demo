/*
 * Winner 
 * 文件名  :LogCompent.java
 * 创建人  :llxiao
 * 创建时间:2018年8月7日
*/

package com.xiao.springcloud.demo.kafka.elk.kafka;

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
public class LogCompent
{

    public void testLog()
    {
        log.info("Hello kafak for logback !!!!!!!!!!!!!!!!!!!!!!!");
    }
}
