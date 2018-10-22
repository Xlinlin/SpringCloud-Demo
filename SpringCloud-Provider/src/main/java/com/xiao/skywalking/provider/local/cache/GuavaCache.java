package com.xiao.skywalking.provider.local.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.util.Assert;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/10/22 14:33
 * @since JDK 1.8
 */
public class GuavaCache extends AbstractValueAdaptingCache
{
    private final String name;
    private final Cache<Object, Object> cache;

    public GuavaCache(String name, Cache<Object, Object> cache)
    {
        this(name, cache, true);
    }

    public GuavaCache(String name, Cache<Object, Object> cache, boolean allowNullValues)
    {
        super(allowNullValues);
        Assert.notNull(name, "Name must not be null");
        Assert.notNull(cache, "Cache must not be null");
        this.name = name;
        this.cache = cache;
    }

    public final String getName()
    {
        return this.name;
    }

    public final Cache<Object, Object> getNativeCache()
    {
        return this.cache;
    }

    public ValueWrapper get(Object key)
    {
        if (this.cache instanceof LoadingCache)
        {
            try
            {
                Object value = ((LoadingCache) this.cache).get(key);
                return this.toValueWrapper(value);
            }
            catch (ExecutionException var3)
            {
                throw new UncheckedExecutionException(var3.getMessage(), var3);
            }
        }
        else
        {
            return super.get(key);
        }
    }

    public <T> T get(Object key, final Callable<T> valueLoader)
    {
        try
        {
            return (T) this.fromStoreValue(this.cache.get(key, () -> GuavaCache.this.toStoreValue(valueLoader.call())));
        }
        catch (ExecutionException var4)
        {
            throw new ValueRetrievalException(key, valueLoader, var4.getCause());
        }
        catch (UncheckedExecutionException var5)
        {
            throw new ValueRetrievalException(key, valueLoader, var5.getCause());
        }
    }

    protected Object lookup(Object key)
    {
        return this.cache.getIfPresent(key);
    }

    public void put(Object key, Object value)
    {
        this.cache.put(key, this.toStoreValue(value));
    }

    public ValueWrapper putIfAbsent(Object key, Object value)
    {
        try
        {
            GuavaCache.PutIfAbsentCallable callable = new GuavaCache.PutIfAbsentCallable(value);
            Object result = this.cache.get(key, callable);
            return callable.called ? null : this.toValueWrapper(result);
        }
        catch (ExecutionException var5)
        {
            throw new IllegalStateException(var5);
        }
    }

    public void evict(Object key)
    {
        this.cache.invalidate(key);
    }

    public void clear()
    {
        this.cache.invalidateAll();
    }

    private class PutIfAbsentCallable implements Callable<Object>
    {
        private final Object value;
        private boolean called;

        public PutIfAbsentCallable(Object value)
        {
            this.value = value;
        }

        public Object call() throws Exception
        {
            this.called = true;
            return GuavaCache.this.toStoreValue(this.value);
        }
    }
}
