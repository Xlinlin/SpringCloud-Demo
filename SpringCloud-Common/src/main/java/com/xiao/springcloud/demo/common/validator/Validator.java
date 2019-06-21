package com.xiao.springcloud.demo.common.validator;

import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * [简要描述]: 参数校验器注解
 * [详细描述]: 结合ParamValidator注解配合使用，ParamValidator定义到DTO数据的属性中
 *
 * @author llxiao
 * @version 1.0, 2018/9/6 15:12
 * @since JDK 1.8
 */
@Retention(RUNTIME)// 注解会在class字节码文件中存在，在运行时可以通过反射获取到
@Inherited//说明子类可以继承父类中的该注解
@Target(ElementType.METHOD)//方法注解
@Documented//说明该注解将被包含在javadoc中
public @interface Validator
{
    String value() default "";
}
