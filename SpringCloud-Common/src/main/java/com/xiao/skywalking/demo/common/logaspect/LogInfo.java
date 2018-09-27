package com.xiao.skywalking.demo.common.logaspect;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.sql.Timestamp;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/9/20 09:53
 * @since JDK 1.8
 */
@Data
public class LogInfo
{
    //失败
    public static final int FAILED = 0;

    //类名
    private String clsName;
    //方法名
    private String methodName;
    //请求参数
    private Object[] params;
    //返回值
    private Object result;
    //调用花费时间 ms
    private Long costTime;
    //其他信息
    private String remark;

    //请求时间
    private Timestamp requestTime;
    //响应时间
    private Timestamp responseTime;

    //成功状态。0成功，1失败
    private int status;

    //错误码
    private int errorCode;
    //错误消息
    private String errorMsg;

    //网络请求信息
    private String serverIp;
    private String serverHost;
    private int serverPort;

    private String clientIp;
    private String clientHost;
    private int clientPort;
    private String requestUri;

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
