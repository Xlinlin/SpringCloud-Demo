package com.xiao.skywalking.demo.common.exception;

/**
 * 异常规范接口
 * @author zhdong
 *
 */
public interface AbstractServiceException {

	/**
     * 获取异常的状态码
     */
	Integer getCode();

    /**
     * 获取异常的提示信息
     */
	String getMessage();
    
}
