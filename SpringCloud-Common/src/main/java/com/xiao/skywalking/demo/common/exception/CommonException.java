package com.xiao.skywalking.demo.common.exception;


/**
 * 异常公共类
 *
 * @author zhdong
 * @date 2018/8/1
 */
public class CommonException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    /**
     * 缺少必填参数，三位错误码后缀，需要结合前缀的业务编码组装成完整的错误码信息
     */
	public static final String REQUIRED_PARAM_SUFFIX = "000";

    /**
     * 参数非法，三位错误码后缀，需要结合前缀的业务编码组装成完整的错误码信息
     */
	public static final String ILLEGAL_PARAM_SUFFIX = "001";

	private Integer code;

    private String errorMessage;

    public CommonException(Integer code, String errorMessage) {
        super(errorMessage);
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public CommonException(AbstractServiceException exception) {
        super(exception.getMessage());
        this.code = exception.getCode();
        this.errorMessage = exception.getMessage();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public static CommonException throwEx(AbstractServiceException e) {
    	throw new CommonException(e);
    }
    
}
