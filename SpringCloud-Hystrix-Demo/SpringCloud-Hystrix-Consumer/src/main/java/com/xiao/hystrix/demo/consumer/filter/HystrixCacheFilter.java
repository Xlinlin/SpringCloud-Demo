package com.xiao.hystrix.demo.consumer.filter;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * [简要描述]: 请求缓存不是只写入一次结果就不再变化的，而是每次请求到达Controller的时候，我们都需要为HystrixRequestContext进行初始化，之前的缓存也就是不存在了，<p>
 * 我们是在同一个请求中保证结果相同，同一次请求中的第一次访问后对结果进行缓存，缓存的生命周期只有一次请求<p>
 * [详细描述]: 初始化HystrixRequestContext <p>
 * Hystrix 缓存使用：<p>
 * 1.需要初始化HystrixRequestContext，并在调用完后清理，使用filter方式进行全局拦截<p>
 * 2.使用CacheResult注解<p>
 * 3.仅限在同一个请求中多次调用feign中生效<p>
 *
 * @author llxiao
 * @version 1.0, 2019/8/8 10:14
 * @since JDK 1.8
 */
@WebFilter(urlPatterns = "/*")
public class HystrixCacheFilter implements Filter
{
    /**
     * Called by the web container to indicate to a filter that it is being
     * placed into service. The servlet container calls the init method exactly
     * once after instantiating the filter. The init method must complete
     * successfully before the filter is asked to do any filtering work.
     * <p>
     * The web container cannot place the filter into service if the init method
     * either:
     * <ul>
     * <li>Throws a ServletException</li>
     * <li>Does not return within a time period defined by the web
     * container</li>
     * </ul>
     *
     * @param filterConfig The configuration information associated with the
     * filter instance being initialised
     * @exception ServletException if the initialisation fails
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {

    }

    /**
     * The <code>doFilter</code> method of the Filter is called by the container
     * each time a request/response pair is passed through the chain due to a
     * client request for a resource at the end of the chain. The FilterChain
     * passed in to this method allows the Filter to pass on the request and
     * response to the next entity in the chain.
     * <p>
     * A typical implementation of this method would follow the following
     * pattern:- <br>
     * 1. Examine the request<br>
     * 2. Optionally wrap the request object with a custom implementation to
     * filter content or headers for input filtering <br>
     * 3. Optionally wrap the response object with a custom implementation to
     * filter content or headers for output filtering <br>
     * 4. a) <strong>Either</strong> invoke the next entity in the chain using
     * the FilterChain object (<code>chain.doFilter()</code>), <br>
     * 4. b) <strong>or</strong> not pass on the request/response pair to the
     * next entity in the filter chain to block the request processing<br>
     * 5. Directly set headers on the response after invocation of the next
     * entity in the filter chain.
     *
     * @param request The request to process
     * @param response The response associated with the request
     * @param chain Provides access to the next filter in the chain for this
     * filter to pass the request and response to for further
     * processing
     * @exception IOException if an I/O error occurs during this filter's
     * processing of the request
     * @exception ServletException if the processing fails for any other reason
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException
    {
        //初始化Hystrix请求上下文
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try
        {
            chain.doFilter(request, response);
        }
        finally
        {
            //关闭Hystrix请求上下文
            context.shutdown();
        }
    }

    /**
     * Called by the web container to indicate to a filter that it is being
     * taken out of service. This method is only called once all threads within
     * the filter's doFilter method have exited or after a timeout period has
     * passed. After the web container calls this method, it will not call the
     * doFilter method again on this instance of the filter. <br>
     * <br>
     * <p>
     * This method gives the filter an opportunity to clean up any resources
     * that are being held (for example, memory, file handles, threads) and make
     * sure that any persistent state is synchronized with the filter's current
     * state in memory.
     */
    @Override
    public void destroy()
    {

    }
}
