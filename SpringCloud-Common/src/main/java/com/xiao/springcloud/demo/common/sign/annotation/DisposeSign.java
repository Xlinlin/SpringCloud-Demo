package com.xiao.springcloud.demo.common.sign.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author mjye
 * @version 1.0, 2020/2/17 23:35
 * @since JDK 1.8
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DisposeSign
{
    /**
     * 是否验签
     *
     * @return 默认验签
     */
    boolean isVerifySign() default true;
}