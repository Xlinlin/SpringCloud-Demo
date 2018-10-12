package com.xiao.skywalking.demo.common.cache.service;

import java.util.Map;
import java.util.Set;

/**
 * [简要描述]: 缓存服务
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/10/11 13:45
 * @since JDK 1.8
 */
public interface CacheService
{

    /**
     * [简要描述]:缓存中获取一个string<br/>
     * [详细描述]:<br/>
     *
     * @param key : K
     * @return java.lang.String
     * llxiao  2018/10/11 - 16:02
     **/
    String get(String key);

    /**
     * [简要描述]:添加一个string到缓存<br/>
     * [详细描述]:<br/>
     *
     * @param key : K
     * @param value : V
     * llxiao  2018/10/11 - 16:02
     **/
    void set(String key, String value);

    /**
     * [简要描述]:添加一个string到缓存<br/>
     * [详细描述]:<br/>
     *
     * @param key : K
     * @param value : V
     * @param leaseTime : 存活时间，单位秒
     * llxiao  2018/10/11 - 16:02
     **/
    void set(String key, String value, long leaseTime);

    /**
     * [简要描述]:获取一个缓存对象<br/>
     * [详细描述]:<br/>
     *
     * @param key :
     * @return java.lang.Object
     * llxiao  2018/10/11 - 15:41
     **/
    <T> T getObject(String key);

    /**
     * [简要描述]:添加一个对象到缓存<br/>
     * [详细描述]:<br/>
     *
     * @param key : key
     * @param value : 值
     * llxiao  2018/10/11 - 15:59
     **/
    <T> void setObject(String key, T value);

    /**
     * [简要描述]:添加一个对象到缓存<br/>
     * [详细描述]:<br/>
     *
     * @param key : key
     * @param value : 值
     * @param leaseTime : 存活时间，单位秒
     * llxiao  2018/10/11 - 15:59
     **/
    <T> void setObject(String key, T value, long leaseTime);

    /**
     * [简要描述]:从map中获取指定key的值<br/>
     * [详细描述]:<br/>
     *
     * @param key : K
     * @param field : 属性名
     * @return java.lang.Object
     * llxiao  2018/10/11 - 16:23
     **/
    <T> T hget(String key, String field);

    /**
     * [简要描述]:设置一个值到map中<br/>
     * [详细描述]:<br/>
     *
     * @param key : K
     * @param field : 属性名
     * @param value : 属性值
     * llxiao  2018/10/11 - 16:32
     **/
    <T> void hset(String key, String field, T value);

    /**
     * [简要描述]:指定key的map<br/>
     * [详细描述]:<br/>
     *
     * @param key : K
     * @return java.util.Map
     * llxiao  2018/10/11 - 19:45
     **/
    <T> Map<String, T> hgetAll(String key);

    /**
     * [简要描述]:设置map集<br/>
     * [详细描述]:<br/>
     *
     * @param key ：key
     * @param maps : 集合
     * llxiao  2018/10/11 - 19:47
     **/
    <T> void hsetAll(String key, Map<String, T> maps);

    /**
     * [简要描述]:Set中添加一个元素<br/>
     * [详细描述]:<br/>
     *
     * @param key : K
     * @param value : 值
     * llxiao  2018/10/11 - 19:57
     **/
    <T> void setAdd(String key, T value);

    /**
     * [简要描述]:获取一个set集合<br/>
     * [详细描述]:<br/>
     *
     * @param key : Key
     * @return Set
     * llxiao  2018/10/11 - 19:59
     **/
    <T> Set<T> setGet(String key);

    /**
     * [简要描述]:Set中移除一个元素<br/>
     * [详细描述]:<br/>
     *
     * @param key : K
     * @param value : 元素
     * llxiao  2018/10/11 - 20:00
     **/
    <T> void setRemove(String key, T value);

}
