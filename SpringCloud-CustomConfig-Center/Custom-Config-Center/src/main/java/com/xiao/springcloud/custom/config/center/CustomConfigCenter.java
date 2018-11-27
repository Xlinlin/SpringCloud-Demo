package com.xiao.springcloud.custom.config.center;

import com.xiao.springcloud.custom.config.center.annotation.CustomEnableConfigServer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * [简要描述]: 配置中心，注册中心一体化
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/11/27 14:35
 * @since JDK 1.8
 */
@SpringBootApplication
@CustomEnableConfigServer
@EnableEurekaServer
public class CustomConfigCenter
{
}
