/*
 * Winner
 * 文件名  :ProtostuffUtil.java
 * 创建人  :llxiao
 * 创建时间:2018年5月2日
 */

package com.xiao.custom.config.client.netty.util;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * [简要描述]:序列化工具类（基于 Protostuff 实现）<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年5月2日
 * @since JDK 1.8
 */
public class ProtostuffUtil
{
    private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();

    private static Objenesis objenesis = new ObjenesisStd(true);

    /**
     * 获取类的schema
     *
     * @param cls
     * @return
     */
    @SuppressWarnings("unchecked")
    private static <T> Schema<T> getSchema(Class<T> cls)
    {
        Schema<T> schema = (Schema<T>) cachedSchema.get(cls);
        if (schema == null)
        {
            schema = RuntimeSchema.createFrom(cls);
            if (schema != null)
            {
                cachedSchema.put(cls, schema);
            }
        }
        return schema;
    }

    /**
     * [简要描述]:序列化（对象 -> 字节数组）<br/>
     * [详细描述]:<br/>
     *
     * @param obj 序列化对象
     * @return 对象字节数组
     */
    @SuppressWarnings("unchecked")
    public static <T> byte[] serialize(T obj)
    {
        Class<T> cls = (Class<T>) obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try
        {
            Schema<T> schema = getSchema(cls);
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        }
        catch (Exception e)
        {
            throw new IllegalStateException(e.getMessage(), e);
        }
        finally
        {
            buffer.clear();
        }
    }

    /**
     * [简要描述]:反序列化（字节数组 -> 对象）<br/>
     * [详细描述]:<br/>
     *
     * @param data 数据
     * @param cls class对象
     * @return class对象
     */
    public static <T> T deserialize(byte[] data, Class<T> cls)
    {
        try
        {
            /*
             * 如果一个类没有参数为空的构造方法时候，那么你直接调用newInstance方法试图得到一个实例对象的时候是会抛出异常的
             * 通过ObjenesisStd可以完美的避开这个问题
             */
            T message = (T) objenesis.newInstance(cls);// 实例化
            Schema<T> schema = getSchema(cls);// 获取类的schema
            ProtostuffIOUtil.mergeFrom(data, message, schema);
            return message;
        }
        catch (Exception e)
        {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
