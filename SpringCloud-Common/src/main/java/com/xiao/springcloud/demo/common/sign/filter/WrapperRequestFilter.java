package com.xiao.springcloud.demo.common.sign.filter;

import lombok.extern.slf4j.Slf4j;
import omni.purcotton.omni.inface.center.common.sign.SignConstants;
import omni.purcotton.omni.inface.center.common.sign.request.BodyReaderHttpServletRequestWrapper;
import omni.purcotton.omni.inface.center.common.sign.service.AppManagerService;
import omni.purcotton.omni.inface.center.common.sign.util.HttpRequestUtils;
import omni.purcotton.omni.inface.center.common.sign.util.SignUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * [简要描述]: 包装reqeust使用
 * [详细描述]:
 *
 * @author xiaolinlin
 * @version 1.0, 2020/2/21 16:49
 * @since JDK 1.8
 */
@Component
@Slf4j
@Order(1)
public class WrapperRequestFilter implements Filter
{
    @Autowired
    private AppManagerService appManagerService;

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
     *     container</li>
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
        // 防止流读取一次后就没有了, 所以需要将流继续写出去
        HttpServletRequest req = (HttpServletRequest) request;
        String appid = req.getHeader(SignConstants.APP_ID);
        String sign = req.getHeader(SignConstants.SIGN_NAME);
        if (StringUtils.isNotBlank(appid) && StringUtils.isNotBlank(sign))
        {
            String appKey = appManagerService.getAppKey(appid);
            if (StringUtils.isNotEmpty(appKey))
            {
                // 计算服务端的签名
                HttpServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper(req);
                String params = HttpRequestUtils.getAllParams(requestWrapper);

                log.info("获取到的参数是===> 【{}】", params);
                log.info("获取到的KEY ====> 【{}】", appKey);
                if (StringUtils.isNotBlank(params))
                {
                    String serverSign = SignUtil.generateSign(params, appKey);
                    requestWrapper.setAttribute(SignConstants.SEVER_SIGN, serverSign);
                    chain.doFilter(requestWrapper, response);
                    return;
                }
            }
        }
        chain.doFilter(request, response);

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
