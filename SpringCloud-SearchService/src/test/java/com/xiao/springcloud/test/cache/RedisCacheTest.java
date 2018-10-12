package com.xiao.springcloud.test.cache;

import com.xiao.skywalking.demo.common.cache.service.CacheService;
import com.xiao.skywalking.demo.common.cache.service.DistributedService;
import com.xiao.springcloud.test.SearchApplicationTest;
import org.junit.Test;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/10/11 20:55
 * @since JDK 1.8
 */
public class RedisCacheTest extends SearchApplicationTest
{

    @Autowired
    private CacheService cacheService;

    @Autowired
    private DistributedService distributedService;

    @Test
    public void testSet()
    {
        //cacheService.set("cacheTest", "hello redisson");
        //        System.out.println(cacheService.get("cacheTest"));
        //        cacheService.set("testLock", "100");
        System.out.println(cacheService.get("testLock"));
    }

    // 上一步设置 testLock的值为100，两台机器或者两个项目 同时执行该分布式测试方法。确定最终结果不会为负数则锁正常
    @Test
    public void testDistributed()
    {
        String key = "lock";
        //执行的业务代码
        for (int i = 0; i < 55; i++)
        {
            RLock rLock = this.distributedService.tryLockAutoRelease(key, 60, 60, TimeUnit.SECONDS);
            int stock = Integer.parseInt(cacheService.get("testLock"));
            if (stock > 0)
            {
                cacheService.set("testLock", (stock - 1) + "");
                System.out.println("test2_:lockkey:" + key + ",stock:" + (stock - 1) + "");
            }
            rLock.unlock();
        }
    }
}
