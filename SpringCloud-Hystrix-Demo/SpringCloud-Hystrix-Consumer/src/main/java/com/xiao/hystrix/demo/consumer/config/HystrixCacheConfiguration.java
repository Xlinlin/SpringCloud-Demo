package com.xiao.hystrix.demo.consumer.config;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * [简要描述]: 初始化扫描 filter
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/8/8 10:18
 * @since JDK 1.8
 */
@ServletComponentScan(basePackages = "com.purcotton.pref.consumer.filter")
@Configuration
public class HystrixCacheConfiguration
{
}
