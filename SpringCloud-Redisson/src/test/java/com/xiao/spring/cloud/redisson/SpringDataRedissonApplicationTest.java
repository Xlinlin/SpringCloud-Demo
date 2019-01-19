package com.xiao.spring.cloud.redisson;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

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
}
