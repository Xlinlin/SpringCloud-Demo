package com.xiao.skywalking.demo.common.gloab.interceptor.fegin;

import feign.RequestInterceptor;
import feign.RequestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * feign远程调用添加header的过滤器
 *
 * @author zhdong
 */
public class CommonFeignHeaderProcessInterceptor implements RequestInterceptor
{

    @Override
    public void apply(RequestTemplate requestTemplate)
    {

        //当前feign远程调用环境不是由http接口发起，例如test单元测试中的feign调用或者项目启动后的feign调用
        HttpServletRequest request = null;

        try
        {
            request = HttpContext.getRequest();
        }
        catch (NullPointerException e)
        {

            //被调环境中不存在request对象，则不往header里添加当前请求环境的header
            return;
        }
        if (request != null)
        {
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null)
            {
                while (headerNames.hasMoreElements())
                {
                    String name = headerNames.nextElement();
                    String values = request.getHeader(name);
                    requestTemplate.header(name, values);
                }
            }
        }
    }
}
