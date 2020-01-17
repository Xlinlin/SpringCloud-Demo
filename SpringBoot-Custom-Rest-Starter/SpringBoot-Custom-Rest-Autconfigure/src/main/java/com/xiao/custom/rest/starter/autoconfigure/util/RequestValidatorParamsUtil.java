package com.xiao.custom.rest.starter.autoconfigure.util;

import com.alibaba.fastjson.JSONObject;
import com.xiao.custom.rest.starter.autoconfigure.dto.Request;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author xiaolinlin
 * @version 1.0, 2020/1/16 15:47
 * @since JDK 1.8
 */
@Slf4j
public class RequestValidatorParamsUtil
{
    public static boolean validateParams(Request request)
    {
        if (null == request || StringUtils.isEmpty(request.getUri()) || null == request.getResponseType())
        {
            log.error("请求参数不能为空：{}", null == request ? "Request is null!" : JSONObject.toJSONString(request));
            return true;
        }
        return false;
    }
}
