package com.xiao.springcloud.demo.common.cache.service.impl;

import com.xiao.springcloud.demo.common.cache.conf.RedissonConfig;
import com.xiao.springcloud.demo.common.cache.service.DistributedService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * [简要描述]: redisson分布式服务
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/10/11 09:19
 * @since JDK 1.8
 */
@Service
@Slf4j
//仅仅在当前上下文中存在某个对象时，才会实例化一个Bean
@ConditionalOnBean(RedissonConfig.class)
//如果存在它修饰的类的bean，则不需要再创建这个bean
public class DistributedServiceRedissonImpl implements DistributedService
{
    @Autowired
    private RedissonClient redissonClient;

    /**
     * [简要描述]:获取分布式非公平可重入锁(加锁)<br/>
     * [详细描述]:阻塞式获取<br/>
     * <p>
     * Redisson内部提供了一个监控锁的看门狗，它的作用是在Redisson实例被关闭前，不断的延长锁的有效期。<br>
     * 默认情况下，看门狗的检查锁的超时时间是30秒钟，也可以通过修改Config.lockWatchdogTimeout来另行指定<br>
     *
     * @param lockKey : key
     * @return org.redisson.api.RLock
     * llxiao  2018/10/11 - 9:06
     **/
    @Override
    public RLock getRLock(String lockKey)
    {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
        return lock;
    }

    /**
     * [简要描述]:非公平可重入锁，并在指定单位时间内自动释放锁，无需手动释放<br/>
     * [详细描述]:<br/>
     *
     * @param lockKey : key
     * @param leaseTime : 存货时间
     * @param unit : 时间单位
     * @return org.redisson.api.RLock
     * llxiao  2018/10/11 - 9:37
     **/
    @Override
    public RLock autoReleaseRLock(String lockKey, long leaseTime, TimeUnit unit)
    {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(leaseTime, unit);
        return lock;
    }

    /**
     * [简要描述]:指定时间内尝试获取非公平可重入锁(加锁)<br/>
     * [详细描述]:<br/>
     *
     * @param lockKey : key
     * @param timeout : 超时时间，单位秒
     * @return org.redisson.api.RLock
     * llxiao  2018/10/11 - 9:51
     **/
    @Override
    public RLock tryLock(String lockKey, long timeout)
    {
        RLock lock = redissonClient.getLock(lockKey);
        boolean flag = false;
        //锁可用立即返回 true,锁不可用立即返回false
        //boolean flag = lock.tryLock();
        try
        {
            // 获取不到锁等待一段时间，如果获取到返回true，获取不到返回false
            flag = lock.tryLock(timeout, TimeUnit.SECONDS);
        }
        catch (InterruptedException e)
        {
            log.warn("尝试获取锁失败，线程中断!", e);
            lock.unlock();
        }

        return flag ? lock : null;
    }

    /**
     * [简要描述]:指定时间内尝试获取非公平可重入锁，获取成功并设置锁自动失效时间(加锁)<br/>
     * [详细描述]:获取不到锁返回null<br/>
     *
     * @param lockKey : key
     * @param timeout : 超时时间，单位秒
     * @param leaseTime : 锁失效时间
     * @param unit : 锁失效单位
     * @return org.redisson.api.RLock
     * llxiao  2018/10/11 - 9:52
     **/
    @Override
    public RLock tryLockAutoRelease(String lockKey, long timeout, long leaseTime, TimeUnit unit)
    {
        RLock lock = redissonClient.getLock(lockKey);
        boolean flag = false;
        try
        {
            flag = lock.tryLock(timeout, leaseTime, unit);
        }
        catch (InterruptedException e)
        {
            log.warn("尝试获取锁失败，线程中断!", e);
            lock.unlock();
        }
        return flag ? lock : null;
    }

    /**
     * [简要描述]:获取分布式可重入公平锁(加锁)<br/>
     * [详细描述]:阻塞式获取，直到获取锁<br/>
     *
     * @param lockKey : key
     * @return org.redisson.api.RLock
     * llxiao  2018/10/11 - 10:40
     **/
    @Override
    public RLock getFairRLock(String lockKey)
    {
        RLock fairLock = redissonClient.getFairLock(lockKey);
        fairLock.lock();
        return fairLock;
    }

    /**
     * [简要描述]:获取可重入公平锁，并在指定单位时间内自动释放锁，无需手动释放<br/>
     * [详细描述]:阻塞式，直到获取锁<br/>
     *
     * @param lockKey : key
     * @param leaseTime : 存货时间
     * @param unit : 时间单位
     * @return org.redisson.api.RLock
     * llxiao  2018/10/11 - 9:37
     **/
    @Override
    public RLock autoReleaseRFairLock(String lockKey, long leaseTime, TimeUnit unit)
    {
        RLock fairLock = redissonClient.getFairLock(lockKey);
        fairLock.lock(leaseTime, unit);
        return fairLock;
    }

    /**
     * [简要描述]:指定时间内尝试获取公平可重入锁<br/>
     * [详细描述]:获取不到锁返回null<br/>
     *
     * @param lockKey : key
     * @param timeout : 超时时间，单位秒
     * @return org.redisson.api.RLock
     * llxiao  2018/10/11 - 9:51
     **/
    @Override
    public RLock tryFairLock(String lockKey, long timeout)
    {
        RLock lock = redissonClient.getFairLock(lockKey);
        boolean flag = false;
        //锁可用立即返回 true,锁不可用立即返回false
        //boolean flag = lock.tryLock();
        try
        {
            // 获取不到锁等待一段时间，如果获取到返回true，获取不到返回false
            flag = lock.tryLock(timeout, TimeUnit.SECONDS);
        }
        catch (InterruptedException e)
        {
            log.warn("尝试获取锁失败，线程中断!", e);
            lock.unlock();
        }

        return flag ? lock : null;
    }

    /**
     * [简要描述]:指定时间内尝试获取公平可重入锁，获取成功并设置锁自动失效时间<br/>
     * [详细描述]:获取不到锁返回null<br/>
     *
     * @param lockKey : key
     * @param timeout : 超时时间，单位秒
     * @param leaseTime : 锁失效时间
     * @param unit : 锁失效单位
     * @return org.redisson.api.RLock
     * llxiao  2018/10/11 - 9:52
     **/
    @Override
    public RLock tryFairLockAutoRelease(String lockKey, long timeout, long leaseTime, TimeUnit unit)
    {
        RLock lock = redissonClient.getLock(lockKey);
        boolean flag = false;
        try
        {
            // 尝试加锁，最多等待timeout秒，上锁以后leaseTime(unit)自动解锁
            flag = lock.tryLock(timeout, leaseTime, unit);
        }
        catch (InterruptedException e)
        {
            log.warn("尝试获取锁失败，线程中断!", e);
            lock.unlock();
        }
        return flag ? lock : null;
    }

    /**
     * [简要描述]:获取读写锁<br/>
     * [详细描述]:<br/>
     *
     * @param lockKey : key
     * @param isWrite : true写锁，false读锁
     * @return org.redisson.api.RReadWriteLock
     * llxiao  2018/10/11 - 10:54
     **/
    @Override
    public RLock getReadWriteLock(String lockKey, boolean isWrite)
    {
        RReadWriteLock rwlock = redissonClient.getReadWriteLock(lockKey);
        RLock rLock;
        if (isWrite)
        {
            rLock = rwlock.writeLock();
        }
        else
        {
            rLock = rwlock.readLock();
        }
        rLock.lock();
        return rLock;
    }

    /**
     * [简要描述]:获取读写锁，指定单位时间内自动过期<br/>
     * [详细描述]:<br/>
     *
     * @param lockKey : key
     * @param isWrite : true写锁，false读锁
     * @param lease : 存活时间
     * @param unit : 时间单位
     * @return org.redisson.api.RReadWriteLock
     * llxiao  2018/10/11 - 10:54
     **/
    @Override
    public RLock autoReleaseReadWriteLock(String lockKey, boolean isWrite, Long lease, TimeUnit unit)
    {
        RReadWriteLock rwlock = redissonClient.getReadWriteLock(lockKey);
        RLock rLock;
        if (isWrite)
        {
            rLock = rwlock.writeLock();
        }
        else
        {
            rLock = rwlock.readLock();
        }
        rLock.lock(lease, unit);
        return rLock;
    }

    /**
     * [简要描述]:指定时间内尝试获取读写锁<br/>
     * [详细描述]:获取不到锁返回null<br/>
     *
     * @param lockKey : key
     * @param isWrite : true写锁，false读锁
     * @param timeout : 超时时间，单位秒
     * @return org.redisson.api.RLock
     * llxiao  2018/10/11 - 9:51
     **/
    @Override
    public RLock tryReadWriteLock(String lockKey, boolean isWrite, long timeout)
    {
        RReadWriteLock rwlock = redissonClient.getReadWriteLock(lockKey);
        RLock rLock;
        if (isWrite)
        {
            rLock = rwlock.writeLock();
        }
        else
        {
            rLock = rwlock.readLock();
        }
        try
        {

            rLock.tryLock(timeout, TimeUnit.SECONDS);
        }
        catch (InterruptedException e)
        {
            log.warn("尝试获取锁失败，线程中断!", e);
            rLock.unlock();
        }
        return rLock;
    }

    /**
     * [简要描述]:指定时间内尝试获取读写锁，获取成功并设置锁自动失效时间<br/>
     * [详细描述]:获取不到锁返回null<br/>
     *
     * @param lockKey : key
     * @param isWrite : true写锁，false读锁
     * @param timeout : 超时时间，单位秒
     * @param leaseTime : 锁失效时间
     * @param unit : 锁失效单位
     * @return org.redisson.api.RLock
     * llxiao  2018/10/11 - 9:52
     **/
    @Override
    public RLock tryReadWriteLockAutoRelease(String lockKey, boolean isWrite, long timeout, long leaseTime,
            TimeUnit unit)
    {
        RLock rLock = this.getReadWriteLock(lockKey, isWrite);
        boolean flag = false;
        try
        {
            // 尝试加锁，最多等待timeout秒，上锁以后leaseTime(unit)自动解锁
            flag = rLock.tryLock(timeout, leaseTime, unit);
        }
        catch (InterruptedException e)
        {
            log.warn("尝试获取锁失败，线程中断!", e);
            rLock.unlock();
        }
        return flag ? rLock : null;
    }

    /**
     * [简要描述]:闭锁（CountDownLatch）<br/>
     * [详细描述]:<br/>
     *
     * @param key : key
     * @param count : 数量
     * @return org.redisson.api.RCountDownLatch
     * llxiao  2018/10/11 - 11:49
     **/
    @Override
    public RCountDownLatch getRCountDownLatch(String key, int count)
    {
        RCountDownLatch rCountDownLatch = redissonClient.getCountDownLatch(key);
        rCountDownLatch.trySetCount(count);
        return rCountDownLatch;
    }

    /**
     * [简要描述]:每次消耗一个分布式闭锁<br/>
     * [详细描述]:<br/>
     *
     * @param key : key
     * llxiao  2018/10/11 - 11:53
     **/
    @Override
    public void countDown(String key)
    {
        RCountDownLatch rCountDownLatch = redissonClient.getCountDownLatch(key);
        rCountDownLatch.countDown();
    }

    /**
     * [简要描述]:解锁<br/>
     * [详细描述]:<br/>
     *
     * @param lockKey : key
     * llxiao  2018/10/11 - 9:42
     **/
    @Override
    public void unRLock(String lockKey)
    {
        RLock lock = redissonClient.getLock(lockKey);
        lock.unlock();
    }

    /**
     * [简要描述]:解锁<br/>
     * [详细描述]:<br/>
     *
     * @param rLock : RLock
     * llxiao  2018/10/11 - 9:42
     **/
    @Override
    public void unRLock(RLock rLock)
    {
        if (null != rLock)
        {
            rLock.unlock();
        }
    }
}
