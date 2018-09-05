package com.xiao.skywalking.demo.common.exception;


/**
 * 异常公共类
 *
 * @author zhdong
 * @date 2018/8/1
 */
public enum CommonExceptionEnum implements AbstractServiceException {

	SYSTEM_ERROR(10000, "系统错误，请联系管理员"),
	TOKEN_HAS_EXPIRED(10010, "token已过期"),
	REMOTE_SERVICE_NULL(10020, "远程调用错误"),
	SERVICE_ERROR(10030, "服务内部错误"),
	IO_ERROR(10050, "读取IO异常"),
	NO_LOGIN(10060, "没有登录或者登录超时");

	CommonExceptionEnum(Integer code, String message) {
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
