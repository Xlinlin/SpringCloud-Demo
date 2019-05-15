package com.xiao.custom.config.web.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HttpSessionStrategy;

/**
 * 分布式缓存机制
 *
 * @author zhdong
 */
@Configuration
//@EnableRedissonHttpSession(maxInactiveIntervalInSeconds = 18000)
public class HttpSessionConfig
{
    /**
     * 基于session缓存的策略
     * 1.如果配置了header
     * 那么就使用header的策略，一般情况针对app和移动端适用
     * 2.cookie策略则适应于pc端浏览器
     *
     * @return
     */
    @Bean
    public HttpSessionStrategy httpSessionStrategy()
    {
        CookieHttpSessionStrategy sessionStrategy = new CookieHttpSessionStrategy();
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookiePath("/");
        sessionStrategy.setCookieSerializer(serializer);
        return sessionStrategy;
    }

    @Bean
    public RequestCache requestCache()
    {
        return new HttpSessionRequestCache();
    }

}
