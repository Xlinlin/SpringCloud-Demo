package com.xiao.springcloud.demo.common.sign;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author xiaolinlin
 * @version 1.0, 2020/2/21 18:22
 * @since JDK 1.8
 */
public interface SignConstants
{
    String GET_METHOD = "GET";
    String POST_METHOD = "POST";

    String ISO_8859_1 = "ISO-8859-1";
    String UTF_8 = "UTF-8";

    String JSON_TYPE = "application/json";

    /*******header params********/

    String SIGN_NAME = "sign";
    String APP_ID = "appid";

    /**
     * 服务端计算出来的签名参数
     */
    String SEVER_SIGN = "serverSign";
}
