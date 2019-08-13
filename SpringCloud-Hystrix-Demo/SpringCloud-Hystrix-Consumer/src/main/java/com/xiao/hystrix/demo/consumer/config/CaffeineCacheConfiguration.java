package com.xiao.hystrix.demo.consumer.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.xiao.hystrix.demo.consumer.common.CacheConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/8/8 10:42
 * @since JDK 1.8
 */
@Configuration
@EnableCaching
@Slf4j
public class CaffeineCacheConfiguration
{
    @Bean
    public CacheManager cacheManager()
    {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        Caffeine caffeine = Caffeine.newBuilder()
                //cache的初始容量值
                .initialCapacity(100)
                // 最大容量值
                .maximumSize(1000)
                // 过期时间 1分钟
                .expireAfterWrite(1, TimeUnit.MINUTES);
        caffeineCacheManager.setAllowNullValues(true);
        caffeineCacheManager.setCaffeine(caffeine);
        caffeineCacheManager.setCacheNames(getNames());
        return caffeineCacheManager;
    }

    private Collection<String> getNames()
    {
        List<String> names = new ArrayList<>(2);
        names.add(CacheConstants.BIZ_DATA_CACHE);
        names.add(CacheConstants.CFG_DATA_CACHE);
        return names;
    }
}
