package com.xiao.skywalking.demo.common.logaspect;

import com.alibaba.fastjson.JSON;
import com.xiao.skywalking.demo.common.exception.CommonException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;

/**
 * [简要描述]: 切面日志处理类<br/>
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/9/2 16:52
 * @since JDK 1.8
 */
@Aspect
@Component
public class LogAspect
{
    /**
     * 日志服务
     */
    @Autowired
    private LogService logService;

    /**
     * [简要描述]:定义一个annotation切入点<br/>
     * [详细描述]:切入点<br/>
     * llxiao  2018/9/2 - 17:02
     **/
    @Pointcut("@annotation(com.xiao.skywalking.demo.common.logaspect.LogAnnotation)")
    public void logAnnotatison()
    {

    }

    // around 切面强化
    @Around("logAnnotatison()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable
    {

        LogInfo logInfo = new LogInfo();
        logInfo.setRequestTime(new Timestamp(System.currentTimeMillis()));
        //通知签名
        Signature signature = joinPoint.getSignature();
        //代理的是哪一个方法
        String methodName = signature.getName();
        //AOP代理类的名字
        String className = joinPoint.getTarget().getClass().getName();
        logInfo.setClsName(className);
        logInfo.setMethodName(methodName);
        //获取方法参数
        Object[] args = joinPoint.getArgs();
        logInfo.setParams(JSON.toJSONString(args));
        Object retrunobj = null;
        //计时工具
        StopWatch clock = new StopWatch();
        Throwable tempE = null;
        clock.start();
        try
        {
            // 注意和finally中的执行顺序 finally是在return中的计算结束返回前执行
            retrunobj = joinPoint.proceed(args);
        }
        catch (Throwable e)
        {
            tempE = e;
            throw e;
        }
        finally
        {
            boolean isError = setResultAndError(joinPoint, logInfo, retrunobj, clock, tempE);
            logInfo.setResponseTime(new Timestamp(System.currentTimeMillis()));
            setHostInfo(logInfo);
            if (isError)
            {
                //记录日志
                logService.error(JSON.toJSONString(logInfo), tempE);
            }
            else
            {
                logService.info(JSON.toJSONString(logInfo));
            }
        }
        return retrunobj;
    }

    /**
     * [简要描述]:设置一些网络参数设置<br/>
     * [详细描述]:<br/>
     *
     * @param logInfo :
     * @return void
     * llxiao  2018/9/20 - 9:30
     **/
    private void setHostInfo(LogInfo logInfo)
    {
        // 本机IP信息
        try
        {
            InetAddress local = InetAddress.getLocalHost();
            if (null != local)
            {
                logInfo.setServerHost(local.getHostName());
                logInfo.setServerIp(local.getHostAddress());
            }
        }
        catch (UnknownHostException e)
        {
            logService.warn("UnknownHostException");
        }

        // 请求IP信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        if (null != request)
        {
            logInfo.setClientIp(request.getRemoteAddr());
            logInfo.setClientHost(request.getRemoteHost());
            logInfo.setRequestUri(request.getRequestURI());
            logInfo.setClientPort(request.getRemotePort());
            logInfo.setServerPort(request.getLocalPort());
        }
    }

    /**
     * [简要描述]:记录日志<br/>
     * [详细描述]:<br/>
     *
     * @param joinPoint :
     * @param logInfo :
     * @param retrunobj :
     * @param clock :
     * @param tempE :
     * @return true 未知异常
     * llxiao  2018/9/8 - 9:53
     **/
    private boolean setResultAndError(ProceedingJoinPoint joinPoint, LogInfo logInfo, Object retrunobj, StopWatch clock,
            Throwable tempE)
    {
        clock.stop();
        logInfo.setResult(JSON.toJSONString(retrunobj));
        logInfo.setCostTime(clock.getTime());
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        //从切面中获取当前方法
        Method method = ms.getMethod();
        //得到了方,提取出他的注解
        LogAnnotation logAnnotation = method.getAnnotation(LogAnnotation.class);
        String customerInfo = logAnnotation.customer();
        //自定义的一些消息
        if (StringUtils.isNotBlank(customerInfo))
        {
            logInfo.setRemark(customerInfo);
        }
        if (null != tempE)
        {
            logInfo.setStatus(LogInfo.FAILED);
            // 业务异常处理不需要打印堆栈信息
            if (tempE instanceof CommonException)
            {
                CommonException ce = (CommonException) tempE;
                logInfo.setErrorCode(ce.getCode());
                logInfo.setErrorMsg(ce.getErrorMessage());
            }
            else
            {
                // 未知异常打印堆栈信息
                logInfo.setErrorMsg(tempE.getMessage());
                return true;
            }
        }
        return false;
    }
}
