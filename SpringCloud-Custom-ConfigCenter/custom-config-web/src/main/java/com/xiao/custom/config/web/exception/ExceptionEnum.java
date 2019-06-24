package com.xiao.custom.config.web.exception;

import com.xiao.springcloud.demo.common.exception.AbstractServiceException;

/**
 * @author zhdong
 * Date 2018/9/2
 */
public enum ExceptionEnum implements AbstractServiceException
{
    REQUEST_TIMEOUT(7500001,"请求超时"),
    PARAM_REQUIRED(7500002,"参数必填"),
    EXCEL_WRITER_IO(7500003,"写入excel出错"),
    EXCEL_READ_IO(750004,"读取excel出错"),
    EXCEL_EXPORT_OVER_MAX(7500005,"导出超过最大数量"),
    NO_LOGIN(7500006,"未登录"),
    EXCEL_DOWN_ERROR(7500007,"下载excel报错");

    ExceptionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private Integer code;

    private String message;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
