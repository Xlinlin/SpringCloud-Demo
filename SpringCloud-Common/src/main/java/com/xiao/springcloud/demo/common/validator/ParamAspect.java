package com.xiao.springcloud.demo.common.validator;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * [简要描述]: 查询校验切面
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/9/6 15:19
 * @since JDK 1.8
 */
@Aspect
@Component
public class ParamAspect
{
    /**
     * [简要描述]:定义一个annotation切入点<br/>
     * [详细描述]:切入点<br/>
     * llxiao  2018/9/2 - 17:02
     **/
    @Pointcut("@annotation(com.purcotton.omni.common.annotation.param.aop.Validator)")
    public void paramValidator()
    {

    }

    // around 切面强化
    @Around("paramValidator()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable
    {
        //获取方法参数
        Object[] args = joinPoint.getArgs();
        for (Object arg : args)
        {
            ParamValidator.validator(arg);
        }
        return joinPoint.proceed(args);
    }

}
