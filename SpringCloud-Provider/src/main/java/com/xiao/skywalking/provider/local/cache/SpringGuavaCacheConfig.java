package com.xiao.skywalking.provider.local.cache;

import com.google.common.cache.CacheBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * [简要描述]: guava+springcache实现本地缓存
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/9/29 17:28
 * @since JDK 1.8
 */
@Configuration
public class SpringGuavaCacheConfig
{
    @Bean
    public CacheManager cacheManager()
    {
        GuavaCacheManager cacheManager = new GuavaCacheManager();
        cacheManager
                // 3S过期时间，初始容量1000个，最大10000个
                .setCacheBuilder(CacheBuilder.newBuilder().expireAfterWrite(3, TimeUnit.SECONDS).initialCapacity(1000)
                        .maximumSize(10000));
        return cacheManager;
    }
}
