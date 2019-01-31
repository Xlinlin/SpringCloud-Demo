package com.xiao.custom.config.server.annotation;

import com.xiao.custom.config.server.config.CustomEnvironmentRepositoryConfiguration;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * [简要描述]: 自定义配置中心注解
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/11/22 15:50
 * @since JDK 1.8
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration
@Import(CustomEnvironmentRepositoryConfiguration.class)
@EnableConfigServer
public @interface CustomEnableConfigServer
{
}
