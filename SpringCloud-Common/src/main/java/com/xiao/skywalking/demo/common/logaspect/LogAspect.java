package com.xiao.skywalking.demo.common.logaspect;

import com.alibaba.fastjson.JSON;
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

import java.lang.reflect.Method;

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
    public Object execute(ProceedingJoinPoint joinPoint)
    {

        StringBuffer sbLog = new StringBuffer();
        //用的最多 通知的签名
        Signature signature = joinPoint.getSignature();
        //代理的是哪一个方法
        String methodName = signature.getName();
        //AOP代理类的名字
        String className = joinPoint.getTarget().getClass().getName();
        sbLog.append("Class: ");
        sbLog.append(className);
        sbLog.append(";Method: ");
        sbLog.append(methodName);
        //获取方法参数
        Object[] args = joinPoint.getArgs();
        sbLog.append(";Params: ");
        sbLog.append(null == args ? "" : JSON.toJSONString(args));
        Object retrunobj = null;
        //计时工具
        StopWatch clock = new StopWatch();
        clock.start();
        Throwable tempE = null;
        try
        {
            // 注意和finally中的执行顺序 finally是在return中的计算结束返回前执行
            retrunobj = joinPoint.proceed(args);
        }
        catch (Throwable e)
        {
            tempE = e;
        }
        clock.stop();
        sbLog.append(";Return: ");
        sbLog.append(retrunobj == null ? "" : JSON.toJSONString(retrunobj));
        sbLog.append(";CostTime: ");
        sbLog.append(clock.getTime());
        sbLog.append("ms");
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        //从切面中获取当前方法
        Method method = ms.getMethod();
        //得到了方,提取出他的注解
        LogAnnotation logAnnotation = method.getAnnotation(LogAnnotation.class);
        String customerInfo = logAnnotation.customer();
        //自定义的一些消息
        if (StringUtils.isNotBlank(customerInfo))
        {
            sbLog.append(";CustomerMsg: ");
            sbLog.append(customerInfo);
        }
        if (null != tempE)
        {
            logService.error(sbLog.toString(), tempE);
        }
        else
        {
            logService.info(sbLog.toString());
        }
        return retrunobj;
    }
}
