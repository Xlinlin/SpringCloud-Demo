package com.xiao.spring.cloud.redisson;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/1/19 09:50
 * @since JDK 1.8
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringDataRedissonApplicationTest
{
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testSimple()
    {
        redisTemplate.opsForValue().set("test", "test11111");
        //        System.out.println(redisTemplate.opsForValue().get("test"));
        Assert.assertEquals("Spring data 集成Redisson测试失败!", "test11111", redisTemplate.opsForValue().get("test"));
    }

    @Test
    public void testBatch()
    {
        RedisSerializer keySer = redisTemplate.getKeySerializer();
        RedisSerializer valueSer = redisTemplate.getValueSerializer();
        RedisSerializer hashKeySer = redisTemplate.getHashKeySerializer();
        RedisSerializer hasValSer = redisTemplate.getHashValueSerializer();

        // 不带返回值使用pipeline  关键是使用第三个参数 pipeline
        redisTemplate.execute(redisConnection ->
        {
            redisConnection.set(keySer.serialize("test"), valueSer.serialize("hello pipeline"));
            return null;
        }, true, true);

        // 带返回值的
        List<String> list = redisTemplate.executePipelined((RedisCallback<String>) redisConnection ->
        {
            byte[] value = redisConnection.get(keySer.serialize("test"));
            return (String) valueSer.deserialize(value);
        });
    }
}
