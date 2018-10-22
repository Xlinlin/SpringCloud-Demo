package com.xiao.skywalking.demo.common.conf;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * [简要描述]: 解决feginclient 调用时使用requestmapping注解被springmvc加载的问题,springcloud2.0的版本据说已经解决这个问题<br>
 * [详细描述]: 可能产生的问题：<br>
 * 1.由于服务消费者并不提供这些接口，对于开发者来说容易造成误解<br>
 * 2.由于加载了一些外部服务的接口定义，还存在与自身接口定义冲突的潜在风险<br>
 *
 * @author llxiao
 * @version 1.0, 2018/9/30 09:58
 * @since JDK 1.8
 */
//@Configuration
//@ConditionalOnClass({ Feign.class })
// springboot 2.0.3版本删除
@Deprecated
public class FeignConfiguration
{
    @Bean
    public WebMvcRegistrations feignWebRegistrations()
    {
        //        return new WebMvcRegistrationsAdapter()
        //        {
        //            @Override
        //            public RequestMappingHandlerMapping getRequestMappingHandlerMapping()
        //            {
        //                return new FeignRequestMappingHandlerMapping();
        //            }
        //        };
        return new WebMvcRegistrations()
        {
            @Override
            public RequestMappingHandlerMapping getRequestMappingHandlerMapping()
            {
                return null;
            }
        };
    }

    private static class FeignRequestMappingHandlerMapping extends RequestMappingHandlerMapping
    {
        @Override
        protected boolean isHandler(Class<?> beanType)
        {
            // 不能被@FeignClient注解修饰的类才会进行解析加载
            //            return super.isHandler(beanType) && !AnnotatedElementUtils.hasAnnotation(beanType, FeignClient.class);
            return false;
        }
    }
}
