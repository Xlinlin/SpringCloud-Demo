package com.xiao.skywalking.demo.common.gloab.interceptor.fegin;

import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiao.skywalking.demo.common.exception.CommonException;
import com.xiao.skywalking.demo.common.exception.CommonExceptionEnum;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 错误解码器
 *
 * @author zhdong
 */
@Slf4j
public class CommonFeignErrorDecoder implements ErrorDecoder
{

    @Override
    public Exception decode(String methodKey, Response response)
    {
        log.error("调用服务返回Response:" + response);
        String responseBody;
        try
        {
            if (response == null || response.body() == null)
            {
                log.error("未得到服务端的响应结果...");
                if (response != null && response.status() == 404)
                {
                    return new CommonException(CommonExceptionEnum.REMOTE_SERVICE_NULL);
                }
                else
                {
                    return new CommonException(CommonExceptionEnum.SERVICE_ERROR);
                }
            }
            responseBody = IoUtil.read(response.body().asInputStream(), "UTF-8");
        }
        catch (IOException e)
        {
            log.error("读取Response响应io错误", e);
            return new CommonException(CommonExceptionEnum.IO_ERROR);
        }

        log.error("服务提供方返回异常结果为：" + responseBody);
        JSONObject parse = JSON.parseObject(responseBody);
        Integer code = parse.getInteger("code");
        String message = parse.getString("message");
        if (message == null)
        {
            message = CommonExceptionEnum.SERVICE_ERROR.getMessage();
        }
        if (code == null)
        {
            //status为spring默认返回的数据
            Integer status = parse.getInteger("status");

            if (status == null)
            {
                return new CommonException(CommonExceptionEnum.SERVICE_ERROR.getCode(), message);
            }
            else
            {
                return new CommonException(status, message);
            }
        }
        else
        {
            return new CommonException(code, message);
        }
    }
}