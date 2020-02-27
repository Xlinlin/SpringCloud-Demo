package com.xiao.springcloud.demo.common.sign.annotation;

import com.xiao.springcloud.demo.common.exception.CommonException;
import com.xiao.springcloud.demo.common.sign.SignConstants;
import com.xiao.springcloud.demo.common.sign.service.AppManagerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author mjye
 * @version 1.0, 2020/2/17 23:39
 * @since JDK 1.8
 */
@Slf4j
@Aspect
@Component
public class DisposeSignService
{

    @Autowired
    private AppManagerService appManagerService;

    @Pointcut("@annotation(omni.purcotton.omni.inface.center.common.sign.annotation.DisposeSign)")
    public void requestAnnotation()
    {
    }

    @Around("requestAnnotation() && @annotation(disposeSign)")
    public Object execute(ProceedingJoinPoint joinPoint, DisposeSign disposeSign) throws Throwable
    {
        Object[] args = joinPoint.getArgs();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        final HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        if (null == request)
        {
            // 无reqeust直接放行
            return joinPoint.proceed(args);
        }

        // 签名校验
        if (disposeSign.isVerifySign())
        {
            String uri = request.getRequestURI();
            // 获取当前的请求头中的签名
            Map<String, String> header = this.assembleHeader(request);
            String clientSign = String.valueOf(header.get(SignConstants.SIGN_NAME));
            if (StringUtils.isBlank(clientSign))
            {
                log.error("请求服务器URL:{}缺少签名参数!", uri);
                throw new CommonException(1002, "缺少签名参数");
            }
            String appId = String.valueOf(header.get(SignConstants.APP_ID));

            if (StringUtils.isBlank(appId))
            {
                log.error("请求服务器URL:{}缺少AppId参数!", uri);
                throw new CommonException(1002, "缺少appid参数!");
            }

            // 暂时保留
            ///Long timestamp = (Long) header.get(TIMESTAMP);

            if (appManagerService.exist(appId))
            {
                String serverSign = (String) request.getAttribute(SignConstants.SEVER_SIGN);
                if (!clientSign.equals(serverSign))
                {
                    log.error("Appid:{} 请求服务器URL: {}，请求签名错误!", appId, uri);
                    log.error("客户端签名：{}，服务端签名：{}", clientSign, serverSign);
                    throw new CommonException(1000, "签名错误!");
                }
            }
            else
            {
                log.error("请求服务器URL:{},App ID: {}非法请求", uri, appId);
                throw new CommonException(1001, "请求非法，无appid");
            }
            return joinPoint.proceed(args);
        }
        else
        {
            // 不需要签名校验，放行
            return joinPoint.proceed(args);
        }

    }

    private Map<String, String> assembleHeader(HttpServletRequest request)
    {
        Map<String, String> header = new HashMap<>(16);
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements())
        {
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            header.put(name, value);
        }
        return header;
    }
}