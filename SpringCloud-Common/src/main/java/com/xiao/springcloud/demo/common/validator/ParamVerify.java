/*
 * Winner 
 * 文件名  :ParamVerify.java
 * 创建人  :llxiao
 * 创建时间:2017年12月27日
*/

package com.xiao.springcloud.demo.common.validator;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * [简要描述]:参数校验注解<br/>
 * [详细描述]:需要结合@CodePrefix注解来获取业务错误码的开头信息<br/>
 *
 * @author llxiao
 * @version 1.0, 2017年12月27日
 * @since JDK1.8
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface ParamVerify
{
    /**
     * 检验的正则表达式
     */
    String regex() default "";

    /**
     * 可以为空
     */
    boolean canBlank() default true;

    /**
     * 最大长度校验
     */
    int maxLeng() default 0;
}
