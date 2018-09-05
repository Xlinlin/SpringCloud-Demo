package com.xiao.skywalking.demo.common.gloab.response;

/**
 * 请求成功的返回
 *
 * @author zhdong
 * @Date 2018/8/1
 */
public class SuccessResponseData extends ResponseData {

    public SuccessResponseData() {
    }

    public SuccessResponseData(Object object) {
        super(true, DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MESSAGE, object);
    }

    public SuccessResponseData(Integer code, String message, Object object) {
        super(true, code, message, object);
    }
}
