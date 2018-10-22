package com.xiao.skywalking.demo.common.gloab.interceptor.fegin;

import cn.hutool.core.bean.BeanUtil;
import com.xiao.skywalking.demo.common.exception.CommonExceptionEnum;
import com.xiao.skywalking.demo.common.gloab.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

/**
 * 重写spring得默认响应提示信息
 *
 * @author zhdong
 */
@Slf4j
public class DefaultCommonErrorAttributes extends DefaultErrorAttributes
{
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace)
    {
        Throwable e = getError(webRequest);
        if (null != e)
        {
            log.error("服务内部异常", e);
        }
        return BeanUtil.beanToMap(ResponseData.error(CommonExceptionEnum.SERVICE_ERROR.getMessage()));
    }
}
