package com.xiao.springcloud.rest.stater.autoconfig.common.log.annotation;

import com.alibaba.fastjson.JSONObject;
import com.xiao.springcloud.rest.stater.autoconfig.common.dto.Request;
import com.xiao.springcloud.rest.stater.autoconfig.common.log.dto.HttpRequestLog;
import com.xiao.springcloud.rest.stater.autoconfig.common.log.service.HttpRequestLogService;
import com.xiao.springcloud.rest.stater.autoconfig.common.util.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * [简要描述]: 请求日志切面
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/4/24 11:39
 * @since JDK 1.8
 */
@Aspect
@Component
@Slf4j
public class RequestLogAspect
{

    /**
     * 请求响应最大长度
     */
    private static final int MAX_PARAMS_LENGTH = 256;

    /**
     * 日志服务
     */
    @Autowired(required = false)
    private HttpRequestLogService httpRequestLogService;

    /**
     * [简要描述]:定义一个annotation切入点<br/>
     * [详细描述]:切入点<br/>
     * llxiao  2018/9/2 - 17:02
     **/
    @Pointcut("@annotation(com.xiao.springcloud.rest.stater.autoconfig.common.log.annotation.RequestLog)")
    public void logAnnotatison()
    {

    }

    /**
     * [简要描述]:around 切面强化<br/>
     * [详细描述]:<br/>
     *
     * @param joinPoint :
     * @return Object
     * llxiao  2019/11/27 - 19:10
     **/
    @Around("logAnnotatison()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable
    {
        HttpRequestLog requestLog = null;
        Object retrunobj = null;
        Object[] args = joinPoint.getArgs();
        if (args.length > 0)
        {

            Object params = args[0];
            if (params instanceof Request)
            {
                Request request = (Request) params;
                Long requestId = request.getRequestId();
                if (null == requestId)
                {
                    requestLog = new HttpRequestLog();
                    requestLog.setCreateTime(new Timestamp(System.currentTimeMillis()));
                    requestLog.setRequest(subParams(JSONObject.toJSONString(request)));
                    requestLog.setUri(request.getUri());
                    requestLog.setParams(subParams(JSONObject.toJSONString(request.getParams())));
                    requestLog.setResponseType(request.getResponseType().getName());
                }
                ThreadLocalUtil.put(HttpRequestLog.REQUEST_LOG, requestLog);
            }

        }

        try
        {
            retrunobj = joinPoint.proceed(args);
            if (null != requestLog)
            {
                requestLog.setResponse(subParams(JSONObject.toJSONString(retrunobj)));
            }
        }
        catch (Throwable e)
        {
            if (null != requestLog)
            {
                requestLog.setErrorMsg(e.getMessage());
            }
            log.error("Http 请求执行错误: ", e);
            throw e;
        }
        finally
        {
            //删除当前线程保存数据，防止内存溢出
            ThreadLocalUtil.remove();
            if (null != httpRequestLogService)
            {
                httpRequestLogService.saveRequestLog(requestLog);
            }
            else
            {
                log.info("Http 执行日志：{}", JSONObject.toJSONString(requestLog));
            }

        }

        return retrunobj;
    }

    /**
     * [简要描述]:参数截取，参数太长超过2000直接用*号代替<br/>
     * [详细描述]:<br/>
     *
     * @param toJsonString :
     * @return java.lang.String
     * llxiao  2019/8/8 - 11:43
     **/
    private String subParams(String toJsonString)
    {
        String params = "";
        if (StringUtils.isNotEmpty(toJsonString))
        {
            if (toJsonString.length() > MAX_PARAMS_LENGTH)
            {
                params = toJsonString.substring(0, MAX_PARAMS_LENGTH);
            }
            else
            {
                params = toJsonString;
            }
        }
        return params;
    }

}
