package com.xiao.springcloud.demo.common.logaspect;

import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * [简要描述]: 自定义日志注解<br/>
 * [详细描述]: 既可以在方法上注解，也可以在类上进行注解<br/>
 *
 * @author llxiao
 * @version 1.0, 2018/9/2 16:46
 * @since JDK 1.8
 */
@Retention(RUNTIME)// 注解会在class字节码文件中存在，在运行时可以通过反射获取到
@Inherited//说明子类可以继承父类中的该注解
@Target(ElementType.METHOD)//既可以在方法上，也可以在类上
@Documented//说明该注解将被包含在javadoc中
public @interface LogAnnotation
{
    String value() default "";

    /**
     * [简要描述]:自定义注解，需要添加一些额外的日志记录<br/>
     * [详细描述]:<br/>
     *
     * @return java.lang.String
     * llxiao  2018/9/2 - 16:50
     **/
    String customer() default "";
}
