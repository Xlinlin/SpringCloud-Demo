package com.xiao.springcloud.demo.common.gloab.interceptor.config;

import com.xiao.springcloud.demo.common.gloab.interceptor.fegin.CommonFeignErrorDecoder;
import com.xiao.springcloud.demo.common.gloab.interceptor.fegin.CommonFeignHeaderProcessInterceptor;
import com.xiao.springcloud.demo.common.gloab.interceptor.fegin.DefaultCommonErrorAttributes;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * feign的错误编码配置（为了feign接收到错误的返回，转化成roses可识别的ServiceException）
 *
 * @author zhdong
 * @Date 2018/8/30 11:11
 */
@Configuration
public class FeignConfig {

    /**
     * roses自定义错误解码器
     */
    //    @Bean
    //    @Scope("prototype")
    //    public Feign.Builder feignHystrixBuilder() {
    //        return HystrixFeign.builder().errorDecoder(new CommonFeignErrorDecoder());
    //    }
    @Bean
    public ErrorDecoder errorDecoder(){
        return new CommonFeignErrorDecoder();
    }

    /**
     * feign请求加上当前请求接口的headers
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return new CommonFeignHeaderProcessInterceptor();
    }

    /**
     * 覆盖spring默认的响应消息格式
     */
    @Bean
    public DefaultCommonErrorAttributes defaultRosesErrorAttributes() {
        return new DefaultCommonErrorAttributes();
    }

}