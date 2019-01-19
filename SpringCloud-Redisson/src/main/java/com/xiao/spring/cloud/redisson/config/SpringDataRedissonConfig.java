package com.xiao.spring.cloud.redisson.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/1/19 09:48
 * @since JDK 1.8
 */
@Configuration
public class SpringDataRedissonConfig
{
    @Bean
    public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redisson)
    {
        return new RedissonConnectionFactory(redisson);
    }

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson(@Value("classpath:/redisson.yml") Resource configFile) throws IOException
    {
        Config config = Config.fromYAML(configFile.getInputStream());
        return Redisson.create(config);
    }

    @Bean
    public RedisTemplate redisTemplate(RedissonConnectionFactory redissonConnectionFactory)
    {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redissonConnectionFactory);
        // 自定义的各种序列化
        //        redisTemplate.setKeySerializer(stringRedisSerializer);
        //        redisTemplate.setValueSerializer(jdkSerializationRedisSerializer);
        //        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        //        redisTemplate.setHashValueSerializer(stringRedisSerializer);
        return redisTemplate;
    }
}
