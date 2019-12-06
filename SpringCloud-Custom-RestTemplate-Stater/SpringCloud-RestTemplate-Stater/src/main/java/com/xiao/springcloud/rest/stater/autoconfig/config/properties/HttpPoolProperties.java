package com.xiao.springcloud.rest.stater.autoconfig.config.properties;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * [简要描述]: http 连接池参数配置
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/11/30 11:09
 * @since JDK 1.8
 */
@Component
@ConfigurationProperties(prefix = "rest.pool")
@ConditionalOnProperty(value = "rest.pool.enable", havingValue = "true")
@Data
public class HttpPoolProperties
{

    private boolean enable;

    /**
     * 最大连接数
     */
    private Integer maxTotal = 20;

    /**
     * 最大路由数
     */
    private Integer defaultMaxPerRoute = 2;

    /**
     * 连接超时时间
     */
    private Integer connectTimeout = 5000;

    /**
     * 请求超时时间
     */
    private Integer connectionRequestTimeout = 1000;

    /**
     * socket超时时间
     */
    private Integer socketTimeout = 6500;

    /**
     * 校验时间
     */
    private Integer validateAfterInactivity = 2000;
}
