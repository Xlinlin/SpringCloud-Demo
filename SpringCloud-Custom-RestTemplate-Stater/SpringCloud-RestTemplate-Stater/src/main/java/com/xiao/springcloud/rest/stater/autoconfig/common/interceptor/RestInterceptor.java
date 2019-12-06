package com.xiao.springcloud.rest.stater.autoconfig.common.interceptor;

import com.xiao.springcloud.rest.stater.autoconfig.common.log.dto.HttpRequestLog;
import com.xiao.springcloud.rest.stater.autoconfig.common.util.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;

import java.io.IOException;
import java.sql.Timestamp;

/**
 * [简要描述]: restTemplate 拦截
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/4/23 16:47
 * @since JDK 1.8
 */
@Slf4j
public class RestInterceptor implements ClientHttpRequestInterceptor
{
    /**
     * Intercept the given request, and return a response. The given {@link ClientHttpRequestExecution} allows
     * the interceptor to pass on the request and response to the next entity in the chain.
     *
     * <p>A typical implementation of this method would follow the following pattern:
     * <ol>
     * <li>Examine the {@linkplain HttpRequest request} and body</li>
     * <li>Optionally {@linkplain HttpRequestWrapper wrap} the request to filter HTTP attributes.</li>
     * <li>Optionally modify the body of the request.</li>
     * <li><strong>Either</strong>
     * <ul>
     * <li>execute the request using {@link ClientHttpRequestExecution#execute(HttpRequest, byte[])},</li>
     * <strong>or</strong>
     * <li>do not execute the request to block the execution altogether.</li>
     * </ul>
     * <li>Optionally wrap the response to filter HTTP attributes.</li>
     * </ol>
     *
     * @param request the request, containing method, URI, and headers
     * @param body the body of the request
     * @param execution the request execution
     * @return the response
     * @exception IOException in case of I/O errors
     */
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException
    {
        HttpRequestLog requestLog = (HttpRequestLog) ThreadLocalUtil.get(HttpRequestLog.REQUEST_LOG);
        if (null != requestLog)
        {
            requestLog.setMethod(request.getMethod().name());
            requestLog.setRequestTime(new Timestamp(System.currentTimeMillis()));
        }

        ClientHttpResponse response = execution.execute(request, body);

        if (null != requestLog)
        {
            requestLog.setHttpStatus(response.getStatusCode().value());
            requestLog.setResponseTime(new Timestamp(System.currentTimeMillis()));
        }
        return response;
    }

}
