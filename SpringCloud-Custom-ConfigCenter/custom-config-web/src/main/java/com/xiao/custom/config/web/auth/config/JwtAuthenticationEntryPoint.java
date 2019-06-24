package com.xiao.custom.config.web.auth.config;

import com.alibaba.fastjson.JSONObject;
import com.xiao.springcloud.demo.common.exception.CommonExceptionEnum;
import com.xiao.springcloud.demo.common.gloab.response.ErrorResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * 登录失败处理
 */
@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable
{

    private static final long serialVersionUID = -8970718410437077606L;

    @Autowired
    private RequestCache requestCache;

    @Value("${config.center.loginUrl}")
    private String loginUrl;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException
    {
        //验证为未登陆状态会进入此方法，认证错误
        log.error("认证失败：" + authException.getMessage());
        log.error("请求url: " + request.getRequestURI());
        cookiesStrategy(request, response, authException);
        //        headerStategy(request,response,authException);
    }

    private void cookiesStrategy(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
    {
        // cookie方式认证直接跳转登录页面
        String xhr = request.getHeader("X-Requested-With");
        // 非ajax请求
        if (StringUtils.isEmpty(xhr))
        {
            requestCache.saveRequest(request, response);
            SavedRequest saveRequest = requestCache.getRequest(request, response);

            RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
            // 获取 跳转url
            String targetUrl = saveRequest.getRedirectUrl();
            log.info("引发跳转的请求是:" + targetUrl);
            try
            {
                redirectStrategy.sendRedirect(request, response, loginUrl + "?refUrl=" + targetUrl);
            }
            catch (IOException e1)
            {
                log.error("JwtAuthenticationTokenFilter,重定向错误", e1);
            }
            return;
        }
        else
        {
            try
            {
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().print(JSONObject.toJSON(ErrorResponseData
                        .error(CommonExceptionEnum.NO_LOGIN.getCode(), CommonExceptionEnum.NO_LOGIN.getMessage())));
            }
            catch (IOException e1)
            {
                log.error("EntryPointUnauthorizedHandler返回错误", e);
            }
        }
    }

    private void headerStategy(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException)
    {
        response.setHeader("Access-Control-Allow-Origin", "*");
        try
        {
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().print(JSONObject.toJSON(ErrorResponseData
                    .error(CommonExceptionEnum.NO_LOGIN.getCode(), CommonExceptionEnum.NO_LOGIN.getMessage())));
        }
        catch (IOException e1)
        {
            log.error("EntryPointUnauthorizedHandler返回错误", e1);
        }
    }
}
