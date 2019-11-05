package com.xiao.skywalking.consumer.common.advice;

import com.xiao.skywalking.consumer.common.response.ResponseData;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * [简要描述]: 统一结果返回处理
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/11/1 23:40
 * @since JDK 1.8
 */
@Configuration
@EnableWebMvc
public class UnifiedReturnAdvice
{
    @RestControllerAdvice
    static class CommonResultAdvice implements ResponseBodyAdvice<Object>
    {

        /**
         * Whether this component supports the given controller method return type
         * and the selected {@code HttpMessageConverter} type.
         *
         * @param returnType the return type
         * @param converterType the selected converter type
         * @return {@code true} if {@link #beforeBodyWrite} should be invoked;
         * {@code false} otherwise
         */
        @Override
        public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType)
        {
            return true;
        }

        /**
         * Invoked after an {@code HttpMessageConverter} is selected and just before
         * its write method is invoked.
         *
         * @param body the body to be written
         * @param returnType the return type of the controller method
         * @param selectedContentType the content type selected through content negotiation
         * @param selectedConverterType the converter type selected to write to the response
         * @param request the current request
         * @param response the current response
         * @return the body that was passed in or a modified (possibly new) instance
         */
        @Override
        public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
                ServerHttpResponse response)
        {
            if (body instanceof ResponseData)
            {
                return body;
            }
            // 针对特殊DTO 不处理，比如mybatis的分页类
            //            if (body instanceof PageInfo)
            //            {
            //                return body;
            //            }
            return new ResponseData<>(body);
        }
    }
}
