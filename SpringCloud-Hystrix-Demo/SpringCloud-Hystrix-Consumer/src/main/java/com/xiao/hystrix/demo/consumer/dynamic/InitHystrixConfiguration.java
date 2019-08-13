package com.xiao.hystrix.demo.consumer.dynamic;

import com.netflix.config.ConfigurationManager;
import com.netflix.config.DynamicConfiguration;
import com.netflix.config.FixedDelayPollingScheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * [简要描述]: 初始化加载动态配置
 * [详细描述]: 通过定时任务动态获取hystrix的配置
 *
 * @author llxiao
 * @version 1.0, 2019/8/5 18:56
 * @since JDK 1.8
 */
@Configuration
@ConditionalOnBean(DynamicConfigSource.class)
@Slf4j
public class InitHystrixConfiguration
{
    @Bean
    public DynamicConfiguration dynamicConfiguration(DynamicConfigSource dynamicConfigSource)
    {
        log.info("初始化Hystrix 动态加载配置服务.....");
        // 延迟3000ms启动，每隔6S中执行一次，是否忽略删除配置
        DynamicConfiguration dynamicConfiguration = new DynamicConfiguration(dynamicConfigSource, new FixedDelayPollingScheduler(
                30 * 100, 60 * 100, false));
        // 安裝后会启动schedel,定时调用DynamicConfigSource.poll()更新配置
        ConfigurationManager.install(dynamicConfiguration);
        return dynamicConfiguration;
    }
}
