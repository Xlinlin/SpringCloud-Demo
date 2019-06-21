package com.xiao.springcloud.demo.common.gloab.interceptor.config;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * fastjson配置类
 *
 * @author zhdong
 */
@Configuration("defaultFastjsonConfig")
@ConditionalOnClass(com.alibaba.fastjson.JSON.class)
@ConditionalOnMissingBean(FastJsonHttpMessageConverter.class)
@ConditionalOnWebApplication
public class FastjsonConfig
{

    @Bean
    public FastJsonHttpMessageConverter fastJsonHttpMessageConverter()
    {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        converter.setFastJsonConfig(fastjsonConfig());
        converter.setSupportedMediaTypes(getSupportedMediaType());
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        return converter;
    }

    /**
     * fastjson的配置
     */
    public FastJsonConfig fastjsonConfig()
    {
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                //SerializerFeature.PrettyFormat,  //格式化输出 ，仅限调试使用
                SerializerFeature.WriteMapNullValue,  //是否输出值为null的字段,默认为false
                SerializerFeature.DisableCircularReferenceDetect, //消除循环引用
                SerializerFeature.WriteNullListAsEmpty //List字段如果为null,输出为[],而非null
        );
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
        fastJsonConfig.setCharset(Charset.forName("utf-8"));
        //不需要将null转换为空字符
//        ValueFilter valueFilter = (object, name, value) -> {
//            return null == value ? "" : value;
//        };
//        fastJsonConfig.setSerializeFilters(valueFilter);
        return fastJsonConfig;
    }

    /**
     * 支持的mediaType类型
     */
    public List<MediaType> getSupportedMediaType()
    {
        ArrayList<MediaType> mediaTypes = new ArrayList<>();

        mediaTypes.add(MediaType.APPLICATION_JSON);
        mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        mediaTypes.add(MediaType.APPLICATION_ATOM_XML);
        mediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        mediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
        mediaTypes.add(MediaType.APPLICATION_PDF);
        mediaTypes.add(MediaType.APPLICATION_RSS_XML);
        mediaTypes.add(MediaType.APPLICATION_XHTML_XML);
        mediaTypes.add(MediaType.APPLICATION_XML);
        mediaTypes.add(MediaType.IMAGE_GIF);
        mediaTypes.add(MediaType.IMAGE_JPEG);
        mediaTypes.add(MediaType.IMAGE_PNG);
        mediaTypes.add(MediaType.TEXT_EVENT_STREAM);
        mediaTypes.add(MediaType.TEXT_HTML);
        mediaTypes.add(MediaType.TEXT_MARKDOWN);
        mediaTypes.add(MediaType.TEXT_PLAIN);
        mediaTypes.add(MediaType.TEXT_XML);

        //增加解析spring boot actuator结果的解析
        mediaTypes.add(MediaType.valueOf("application/vnd.spring-boot.actuator.v2+json"));

        return mediaTypes;
    }

}
