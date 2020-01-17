package com.xiao.custom.rest.starter.autoconfigure.log.annotation;

import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * [简要描述]: 请HTTP求日志注解
 * [详细描述]:
 * Retention 注解会在class字节码文件中存在，在运行时可以通过反射获取到
 * Inherited 说明子类可以继承父类中的该注解
 * Target 既可以在方法上，也可以在类上
 * Documented说明该注解将被包含在javadoc中
 *
 * @author llxiao
 * @version 1.0, 2019/4/24 11:40
 * @since JDK 1.8
 */
@Retention(RUNTIME)
@Inherited
@Target(ElementType.METHOD)
@Documented
public @interface RequestLog
{
    String value() default "";
}
