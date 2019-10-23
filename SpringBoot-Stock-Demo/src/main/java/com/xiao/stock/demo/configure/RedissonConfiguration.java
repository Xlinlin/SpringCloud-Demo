package com.xiao.stock.demo.configure;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/10/17 10:49
 * @since JDK 1.8
 */
@Configuration
public class RedissonConfiguration
{
    @Value("${redisson.host}")
    private String redisHost;
    @Value("${redisson.password}")
    private String password;

    @Value("${thread.pool.core.size:50}")
    private int coreSize;
    @Value("${thread.pool.max.size:100}")
    private int maxSize;
    @Value("${thread.pool.queue.capacity:10000}")
    private int queueCapacity;

    @Bean
    public Config config()
    {
        Config config = new Config();
        config.useSingleServer().setAddress(redisHost);
        config.useSingleServer().setPassword(password);
        return config;
    }

    @Bean
    public RedissonClient redissonClient(Config config)
    {
        return Redisson.create(config);
    }

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor()
    {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(coreSize);
        threadPoolTaskExecutor.setMaxPoolSize(maxSize);
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        threadPoolTaskExecutor.setKeepAliveSeconds(60);
        threadPoolTaskExecutor.setThreadNamePrefix("Stock-Demo-");
        return threadPoolTaskExecutor;
    }
}
