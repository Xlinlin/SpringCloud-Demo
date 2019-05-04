/*
 * Winner 
 * 文件名  :MqttServiceApp.java
 * 创建人  :llxiao
 * 创建时间:2018年4月16日
*/

package com.skywalking.mqtt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年4月16日
 * @since 项目名称 项目版本
 */
@SpringBootApplication
@ServletComponentScan
public class MqttServiceApp extends SpringBootServletInitializer
{
    /**
     * [简要描述]:Servlet初始化器<br/>
     * [详细描述]:<br/>
     * 
     * @author llxiao
     * @param application
     * @return
     * @see
     *      org.springframework.boot.web.support.SpringBootServletInitializer#configure(org.springframework.boot.builder.SpringApplicationBuilder)
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
    {
        return application.sources(MqttServiceApp.class);
    }

    public static void main(String[] args) throws Exception
    {
        SpringApplication.run(MqttServiceApp.class, args);
    }
}
