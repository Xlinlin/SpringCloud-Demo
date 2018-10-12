package com.xiao.skywalking.demo.common.cache.service;

import org.redisson.api.RCountDownLatch;
import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * [简要描述]: 分布式服务：<br>
 * [详细描述]: 分布式锁、分布式同步器等 <br>
 * 公平锁：排队获取锁，保证先来先服务
 * 非公平锁：类似随机，即新来的线程可能会优先于已经在队列中的线程获取到锁，但只要进入了队列就会保证公平。ReentrantLock默认实现为非公平锁
 * 读写锁：允许多个多锁，但只能又一个写锁。读写、写写互斥
 * 闭锁：CountDownLatch
 *
 * @author llxiao
 * @version 1.0, 2018/10/11 09:04
 * @since JDK 1.8
 */
public interface DistributedService
{
    /**
     * [简要描述]:获取分布式非公平可重入锁<br>
     * [详细描述]:阻塞式获取，直到获取锁<br>
     * <p>
     * Redisson内部提供了一个监控锁的看门狗，它的作用是在Redisson实例被关闭前，不断的延长锁的有效期。<br>
     * 默认情况下，看门狗的检查锁的超时时间是30秒钟，也可以通过修改Config.lockWatchdogTimeout来另行指定<br>
     *
     * @param lockKey : key
     * @return org.redisson.api.RLock
     * llxiao  2018/10/11 - 9:06
     **/
    RLock getRLock(String lockKey);

    /**
     * [简要描述]:非公平可重入锁，并在指定单位时间内自动释放锁，无需手动释放<br/>
     * [详细描述]:阻塞式，直到获取锁<br/>
     *
     * @param lockKey : key
     * @param leaseTime : 存货时间
     * @param unit : 时间单位
     * @return org.redisson.api.RLock
     * llxiao  2018/10/11 - 9:37
     **/
    RLock autoReleaseRLock(String lockKey, long leaseTime, TimeUnit unit);

    /**
     * [简要描述]:指定时间内尝试获取非公平可重入锁<br/>
     * [详细描述]:获取不到锁返回null<br/>
     *
     * @param lockKey : key
     * @param timeout : 超时时间，单位秒
     * @return org.redisson.api.RLock
     * llxiao  2018/10/11 - 9:51
     **/
    RLock tryLock(String lockKey, long timeout);

    /**
     * [简要描述]:指定时间内尝试获取非公平可重入锁，获取成功并设置锁自动失效时间<br/>
     * [详细描述]:获取不到锁返回null<br/>
     *
     * @param lockKey : key
     * @param timeout : 超时时间，单位秒
     * @param leaseTime : 锁失效时间
     * @param unit : 锁失效单位
     * @return org.redisson.api.RLock
     * llxiao  2018/10/11 - 9:52
     **/
    RLock tryLockAutoRelease(String lockKey, long timeout, long leaseTime, TimeUnit unit);

    /**
     * [简要描述]:获取分布式可重入公平锁<br/>
     * [详细描述]:阻塞式获取，直到获取锁<br/>
     *
     * @param lockKey : key
     * @return org.redisson.api.RLock
     * llxiao  2018/10/11 - 10:40
     **/
    RLock getFairRLock(String lockKey);

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
    RLock autoReleaseRFairLock(String lockKey, long leaseTime, TimeUnit unit);

    /**
     * [简要描述]:指定时间内尝试获取公平可重入锁<br/>
     * [详细描述]:获取不到锁返回null<br/>
     *
     * @param lockKey : key
     * @param timeout : 超时时间，单位秒
     * @return org.redisson.api.RLock
     * llxiao  2018/10/11 - 9:51
     **/
    RLock tryFairLock(String lockKey, long timeout);

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
    RLock tryFairLockAutoRelease(String lockKey, long timeout, long leaseTime, TimeUnit unit);

    /**
     * [简要描述]:获取读写锁<br/>
     * [详细描述]:<br/>
     *
     * @param lockKey : key
     * @param isWrite : true写锁，false读锁
     * @return org.redisson.api.RReadWriteLock
     * llxiao  2018/10/11 - 10:54
     **/
    RLock getReadWriteLock(String lockKey, boolean isWrite);

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
    RLock autoReleaseReadWriteLock(String lockKey, boolean isWrite, Long lease, TimeUnit unit);

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
    RLock tryReadWriteLock(String lockKey, boolean isWrite, long timeout);

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
    RLock tryReadWriteLockAutoRelease(String lockKey, boolean isWrite, long timeout, long leaseTime, TimeUnit unit);

    /**
     * [简要描述]:获取一点数量的分布式闭锁（CountDownLatch）<br/>
     * [详细描述]:<br/>
     *
     * @param key : key
     * @param count : 数量
     * @return org.redisson.api.RCountDownLatch
     * llxiao  2018/10/11 - 11:49
     **/
    RCountDownLatch getRCountDownLatch(String key, int count);

    /**
     * [简要描述]:每次消耗一个分布式闭锁<br/>
     * [详细描述]:<br/>
     *
     * @param key : key
     * llxiao  2018/10/11 - 11:53
     **/
    void countDown(String key);

    /**
     * [简要描述]:可重入锁解锁<br/>
     * [详细描述]:<br/>
     *
     * @param lockKey : key
     * llxiao  2018/10/11 - 9:42
     **/
    void unRLock(String lockKey);

    /**
     * [简要描述]:可重入锁解锁<br/>
     * [详细描述]:<br/>
     *
     * @param rLock : RLock
     * llxiao  2018/10/11 - 9:42
     **/
    void unRLock(RLock rLock);
}
