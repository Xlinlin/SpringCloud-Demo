package com.xiao.springcloud.demo.common.cache.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.xiao.springcloud.demo.common.cache.conf.RedissonConfig;
import com.xiao.springcloud.demo.common.cache.dto.EntryDto;
import com.xiao.springcloud.demo.common.cache.service.CacheService;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import java.util.List;
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
        if (null != redissonClient && StringUtils.isNotBlank(key))
        {
            RBucket<String> bucket = redissonClient.getBucket(key);
            return bucket.get();
        }
        return "";
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
        if (redissonClient != null && StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value))
        {
            RBucket<String> bucket = redissonClient.getBucket(key);
            bucket.set(value);
        }
    }

    /**
     * [简要描述]:批量设置string到缓存<br/>
     * [详细描述]:<br/>
     *
     * @param list : 批量string数据
     * llxiao  2018/10/15 - 10:07
     **/
    @Override
    public void batchSet(List<EntryDto<String>> list)
    {
        if (redissonClient != null && CollectionUtil.isNotEmpty(list))
        {
            RBatch batch = redissonClient.createBatch();
            for (EntryDto<String> entry : list)
            {
                batch.getBucket(entry.getKey()).setAsync(entry.getValue());
            }
            batch.execute();
        }
    }

    /**
     * [简要描述]:添加一个string到缓存<br/>
     * [详细描述]:<br/>
     *
     * @param key : K
     * @param value : V
     * @param leaseTime : 存活时间，单位秒
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
     * llxiao  2018/10/11 - 15:59
     **/
    @Override
    public <T> void setObject(String key, T value)
    {
        RBucket<T> bucket = redissonClient.getBucket(key);
        bucket.set(value);
    }

    /**
     * [简要描述]:批量设置Object到缓存<br/>
     * [详细描述]:<br/>
     *
     * @param objs : 数据集合
     * llxiao  2018/10/15 - 10:08
     **/
    @Override
    public <T> void batchSetObj(List<EntryDto<Object>> objs)
    {
        if (redissonClient != null && CollectionUtil.isNotEmpty(objs))
        {
            RBatch batch = redissonClient.createBatch();
            for (EntryDto<Object> entry : objs)
            {
                batch.getBucket(entry.getKey()).setAsync(entry.getValue());
            }
            batch.execute();
        }
    }

    /**
     * [简要描述]:添加一个对象到缓存<br/>
     * [详细描述]:<br/>
     *
     * @param key : key
     * @param value : 值
     * @param leaseTime : 存活时间，单位秒
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
     * @return Map
     * llxiao  2018/10/11 - 19:45
     **/
    @Override
    public <T> Map<String, T> hgetAll(String key)
    {
        return (Map) redissonClient.getMap(key).readAllMap();
    }

    /**
     * [简要描述]:设置map集<br/>
     * [详细描述]:<br/>
     *
     * @param key ：key
     * @param maps : 集合
     * llxiao  2018/10/11 - 19:47
     **/
    @Override
    public <T> void hsetAll(String key, Map<String, T> maps)
    {
        RMap<String, T> map = redissonClient.getMap(key);
        map.putAll(maps);
    }

    /**
     * [简要描述]:批量hset<br/>
     * [详细描述]:<br/>
     *
     * @param maps :  KEY,MAP集合
     * llxiao  2018/10/15 - 11:16
     **/
    @Override
    public <T> void batchHset(List<EntryDto<Map<String, Object>>> maps)
    {
        if (null != redissonClient && CollectionUtil.isNotEmpty(maps))
        {
            RBatch batch = redissonClient.createBatch();
            for (EntryDto<Map<String, Object>> entry : maps)
            {
                batch.getMap(entry.getKey()).putAllAsync(entry.getValue());
            }
            batch.execute();
        }
    }

    /**
     * [简要描述]:Set中添加一个元素<br/>
     * [详细描述]:<br/>
     *
     * @param key : K
     * @param value : 值
     * llxiao  2018/10/11 - 19:57
     **/
    @Override
    public <T> void addSet(String key, T value)
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
    public <T> Set<T> getSet(String key)
    {
        return (Set) redissonClient.getSet(key).readAll();
    }

    /**
     * [简要描述]:Set中移除一个元素<br/>
     * [详细描述]:<br/>
     *
     * @param key : K
     * @param value : 元素
     **/
    @Override
    public <T> void removeSet(String key, T value)
    {
        redissonClient.getSet(key).remove(value);
    }

    /**
     * [简要描述]:批量从缓存获取对象集合<br/>
     * [详细描述]:<br/>
     *
     * @param keys : KEY
     * @return java.util.List<T>
     * llxiao  2018/10/15 - 10:00
     **/
    @Override
    public <T> List<T> batchGet(List<String> keys)
    {
        RBatch batch = redissonClient.createBatch();
        for (String key : keys)
        {
            batch.getBucket(key).getAsync();
        }
        BatchResult result = batch.execute();
        return result.getResponses();
    }

    /**
     * [简要描述]:批量从缓存获取MAP集合<br/>
     * [详细描述]:<br/>
     *
     * @param keys : KEY
     * @return java.util.List<java.util.Map>
     * llxiao  2018/10/15 - 10:01
     **/
    @Override
    public <T> List<Map<String, T>> batchGetMap(List<String> keys)
    {
        RBatch batch = redissonClient.createBatch();
        for (String key : keys)
        {
            batch.getMap(key).readAllMapAsync();
        }
        BatchResult result = batch.execute();
        return result.getResponses();
    }

    /**
     * [简要描述]:删除一个对象<br/>
     * [详细描述]:<br/>
     *
     * @param key : key
     * llxiao  2018/10/15 - 14:40
     **/
    @Override
    public void delObject(String key)
    {
        RBucket bucket = redissonClient.getBucket(key);
        bucket.deleteAsync();
    }

    /**
     * [简要描述]:清空一个map<br/>
     * [详细描述]:<br/>
     *
     * @param key : KEY
     * llxiao  2018/10/15 - 14:41
     **/
    @Override
    public void delMap(String key)
    {
        RMap map = redissonClient.getMap(key);
        map.deleteAsync();
    }

}
