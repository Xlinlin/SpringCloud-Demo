package com.xiao.springcloud.demo.common.gloab.interceptor.fegin;

import cn.hutool.core.bean.BeanUtil;
import com.xiao.springcloud.demo.common.exception.CommonExceptionEnum;
import com.xiao.springcloud.demo.common.gloab.response.ResponseData;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.web.context.request.RequestAttributes;

import java.util.Map;

/**
 * 重写spring得默认响应提示信息
 *
 * @author zhdong
 * @Date 2018/8/30 23:14
 */
public class DefaultCommonErrorAttributes extends DefaultErrorAttributes
{

    @Override
    public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace) {
        return BeanUtil.beanToMap(ResponseData.error(CommonExceptionEnum.SERVICE_ERROR.getMessage()));

    }
}
