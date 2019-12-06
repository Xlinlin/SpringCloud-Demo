package com.xiao.springcloud.rest.stater.autoconfig.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * [简要描述]: ThreadLocalUtil
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/4/24 09:25
 * @since JDK 1.8
 */
public class ThreadLocalUtil
{
    private static final ThreadLocal<Map<String, Object>> context = ThreadLocal.withInitial(() -> new HashMap<>());

    public static Map<String, Object> getThreadLocal()
    {
        return context.get();
    }

    /**
     * [简要描述]:从ThreadLocal中获取一个线程变量<br/>
     * [详细描述]:不存在，返回null<br/>
     *
     * @param key :
     * @return java.lang.Object
     * llxiao  2019/4/24 - 9:43
     **/
    public static Object get(String key)
    {
        Map<String, Object> map = context.get();
        if (null != map)
        {
            return map.get(key);
        }
        return null;
    }

    /**
     * [简要描述]:设置一个键值对到ThreadLocal中<br/>
     * [详细描述]:<br/>
     *
     * @param key :
     * @param value :
     * llxiao  2019/4/24 - 9:42
     **/
    public static void put(String key, Object value)
    {
        Map<String, Object> map = context.get();
        if (null == map)
        {
            map = new HashMap<>();
            context.set(map);
        }
        map.put(key, value);
    }

    /**
     * [简要描述]:从ThreadLocal的当前线程中删除一个key<br/>
     * [详细描述]:<br/>
     *
     * @param key :
     * @return void
     * llxiao  2019/4/24 - 9:41
     **/
    public static void remove(String key)
    {
        Map<String, Object> map = context.get();
        if (null != map)
        {
            map.remove(key);
        }
    }

    /**
     * [简要描述]:从ThreadLocal中移除当前线程的变量<br/>
     * [详细描述]:<br/>
     * <p>
     * llxiao  2019/4/24 - 9:41
     **/
    public static void remove()
    {
        context.remove();
    }
}
