package com.xiao.skywalking.consumer.common.response;

import lombok.NoArgsConstructor;

/**
 * 请求失败的返回
 */
@NoArgsConstructor
public class ErrorResponseData<T> extends ResponseData<T> {

    public ErrorResponseData(String message) {
        super(false, DEFAULT_ERROR_CODE, message, null);
    }

    public ErrorResponseData(Integer code, String message) {
        super(false, code, message, null);
    }

    public ErrorResponseData(Integer code, String message, T data) {
        super(false, code, message, data);
    }
}
