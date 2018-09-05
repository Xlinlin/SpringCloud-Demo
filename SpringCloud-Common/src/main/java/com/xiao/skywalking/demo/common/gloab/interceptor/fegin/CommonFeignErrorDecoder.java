package com.xiao.skywalking.demo.common.gloab.interceptor.fegin;

import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.purcotton.omni.common.exception.CommonException;
import com.purcotton.omni.common.exception.CommonExceptionEnum;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.IOException;

/**
 *  错误解码器
 *
 * @author zhdong
 * @Date 2018/8/30 23:14
 */
public class CommonFeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        String resposeBody;
        try {
            if (response == null || response.body() == null) {
                if (response != null && response.status() == 404) {
                    return new CommonException(CommonExceptionEnum.REMOTE_SERVICE_NULL);
                } else {
                    return new CommonException(CommonExceptionEnum.SERVICE_ERROR);
                }
            }
            resposeBody = IoUtil.read(response.body().asInputStream(), "UTF-8");
        } catch (IOException e) {
            return new CommonException(CommonExceptionEnum.IO_ERROR);
        }

        JSONObject parse = JSON.parseObject(resposeBody);
        Integer code = parse.getInteger("code");
        String message = parse.getString("message");
        if (message == null) {
            message = CommonExceptionEnum.SERVICE_ERROR.getMessage();
        }
        if (code == null) {

            //status为spring默认返回的数据
            Integer status = parse.getInteger("status");

            if (status == null) {
                return new CommonException(CommonExceptionEnum.SERVICE_ERROR.getCode(), message);
            } else {
                return new CommonException(status, message);
            }
        } else {
            return new CommonException(code, message);
        }
    }
}