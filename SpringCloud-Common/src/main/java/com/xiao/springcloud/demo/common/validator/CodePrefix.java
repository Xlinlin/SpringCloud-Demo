package com.xiao.springcloud.demo.common.validator;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * [简要描述]: 从类注解中获取业务编码的开头信息
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/8/31 10:54
 * @since JDK 1.8
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface CodePrefix
{
    /**
     * 业务开头编码
     */
    int moduleCode() default 0;
}
