package com.xiao.skywalking.demo.common.cache.service.impl;

import com.xiao.skywalking.demo.common.cache.conf.RedissonConfig;
import com.xiao.skywalking.demo.common.cache.service.CacheService;
import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * [简要描述]: 缓存服务
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/10/11 15:42
 * @since JDK 1.8
 */
@Service
//仅仅在当前上下文中存在某个对象时，才会实例化一个Bean
@ConditionalOnBean(RedissonConfig.class)
//如果存在它修饰的类的bean，则不需要再创建这个bean
//@ConditionalOnMissingBean
public class CacheServiceRedisImpl implements CacheService
{

    @Autowired
    private RedissonClient redissonClient;

    /**
     * [简要描述]:缓存中获取一个string<br/>
     * [详细描述]:<br/>
     *
     * @param key : K
     * @return java.lang.String
     * llxiao  2018/10/11 - 16:02
     **/
    @Override
    public String get(String key)
    {
        RBucket<String> bucket = redissonClient.getBucket(key);
        return bucket.get();
    }

    /**
     * [简要描述]:添加一个string到缓存<br/>
     * [详细描述]:<br/>
     *
     * @param key : K
     * @param value : V
     * @return void
     * llxiao  2018/10/11 - 16:02
     **/
    @Override
    public void set(String key, String value)
    {
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.set(value);
    }

    /**
     * [简要描述]:添加一个string到缓存<br/>
     * [详细描述]:<br/>
     *
     * @param key : K
     * @param value : V
     * @param leaseTime : 存活时间，单位秒
     * @return void
     * llxiao  2018/10/11 - 16:02
     **/
    @Override
    public void set(String key, String value, long leaseTime)
    {
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.set(value, leaseTime, TimeUnit.SECONDS);
    }

    /**
     * [简要描述]:获取一个缓存对象<br/>
     * [详细描述]:<br/>
     *
     * @param key :
     * @return java.lang.Object
     * llxiao  2018/10/11 - 15:41
     **/
    @Override
    public <T> T getObject(String key)
    {
        RBucket<T> bucket = redissonClient.getBucket(key);
        return null != bucket ? bucket.get() : null;
    }

    /**
     * [简要描述]:添加一个对象到缓存<br/>
     * [详细描述]:<br/>
     *
     * @param key : key
     * @param value : 值
     * @return void
     * llxiao  2018/10/11 - 15:59
     **/
    @Override
    public <T> void setObject(String key, T value)
    {
        RBucket<T> bucket = redissonClient.getBucket(key);
        bucket.set(value);
    }

    /**
     * [简要描述]:添加一个对象到缓存<br/>
     * [详细描述]:<br/>
     *
     * @param key : key
     * @param value : 值
     * @param leaseTime : 存活时间，单位秒
     * @return void
     * llxiao  2018/10/11 - 15:59
     **/
    @Override
    public <T> void setObject(String key, T value, long leaseTime)
    {
        RBucket<T> bucket = redissonClient.getBucket(key);
        bucket.set(value, leaseTime, TimeUnit.SECONDS);
    }

    /**
     * [简要描述]:<br/>
     * [详细描述]:<br/>
     *
     * @param key :
     * @param field :
     * @return java.lang.Object
     * llxiao  2018/10/11 - 16:23
     **/
    @Override
    public <T> T hget(String key, String field)
    {
        RMap<String, T> map = redissonClient.getMap(key);
        return map.get(field);
    }

    /**
     * [简要描述]:设置一个值到map中<br/>
     * [详细描述]:<br/>
     *
     * @param key : K
     * @param field : 属性名
     * @param value : 属性值
     * @return void
     * llxiao  2018/10/11 - 16:32
     **/
    @Override
    public <T> void hset(String key, String field, T value)
    {
        RMap<String, T> map = redissonClient.getMap(key);
        map.put(field, value);
    }

    /**
     * [简要描述]:指定key的map<br/>
     * [详细描述]:<br/>
     *
     * @param key : K
     * @return java.util.Map<java.lang.String , T>
     * llxiao  2018/10/11 - 19:45
     **/
    @Override
    public <T> Map<String, T> hgetAll(String key)
    {
        return redissonClient.getMap(key);
    }

    /**
     * [简要描述]:设置map集<br/>
     * [详细描述]:<br/>
     *
     * @param key ：key
     * @param maps : 集合
     * @return void
     * llxiao  2018/10/11 - 19:47
     **/
    @Override
    public <T> void hsetAll(String key, Map<String, T> maps)
    {
        RMap<String, T> map = redissonClient.getMap(key);
        map.putAll(maps);
    }

    /**
     * [简要描述]:Set中添加一个元素<br/>
     * [详细描述]:<br/>
     *
     * @param key : K
     * @param value : 值
     * @return void
     * llxiao  2018/10/11 - 19:57
     **/
    @Override
    public <T> void setAdd(String key, T value)
    {
        RSet<T> set = redissonClient.getSet(key);
        set.add(value);
    }

    /**
     * [简要描述]:获取一个set集合<br/>
     * [详细描述]:<br/>
     *
     * @param key : Key
     * @return T
     * llxiao  2018/10/11 - 19:59
     **/
    @Override
    public <T> Set<T> setGet(String key)
    {
        return redissonClient.getSet(key);
    }

    /**
     * [简要描述]:Set中移除一个元素<br/>
     * [详细描述]:<br/>
     *
     * @param key : K
     * @param value : 元素
     **/
    @Override
    public <T> void setRemove(String key, T value)
    {
        redissonClient.getSet(key).remove(value);
    }

}
