package com.xiao.springcloud.rest.stater.autoconfig.config.properties;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * [简要描述]: ok http 参数配置
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/11/30 11:09
 * @since JDK 1.8
 */
@Component
@ConfigurationProperties("rest.okhttp")
@ConditionalOnProperty(value = "rest.okhttp.enable", havingValue = "true")
@Data
public class OkHttpProperties
{

    private boolean enable;

    /**
     * 连接超时时间
     */
    private int connectionTimeout = 12000;

    /**
     * 读超时时间
     */
    private int readTimeout = 300000;

    /**
     * 写超时时间
     */
    private int writeTimeout = 120000;
}
