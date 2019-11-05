package com.xiao.skywalking.consumer.common.response;

import lombok.NoArgsConstructor;

/**
 * 请求成功的返回
 */
@NoArgsConstructor
public class SuccessResponseData<T> extends ResponseData<T> {

    public SuccessResponseData(T data) {
        super(true, DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MESSAGE, data);
    }

    public SuccessResponseData(Integer code, String message, T data) {
        super(true, code, message, data);
    }
}
