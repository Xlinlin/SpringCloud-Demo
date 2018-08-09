/*
 * Winner 
 * 文件名  :ConfigureApplication.java
 * 创建人  :llxiao
 * 创建时间:2018年8月9日
*/

package com.xiao.springcloud.configure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年8月9日
 * @since JDK 1.8
 */
@SpringBootApplication
// 配置中心
@EnableConfigServer
public class ConfigureApplication
{

    /**
     * [简要描述]:<br/>
     * [详细描述]:<br/>
     * 
     * @author llxiao
     * @param args
     */
    public static void main(String[] args)
    {
        SpringApplication.run(ConfigureApplication.class, args);
    }

}
