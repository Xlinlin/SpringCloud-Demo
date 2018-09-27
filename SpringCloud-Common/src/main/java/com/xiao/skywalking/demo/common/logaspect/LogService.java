package com.xiao.skywalking.demo.common.logaspect;

/**
 * [简要描述]: 日志服务 <br/>
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/9/2 16:53
 * @since JDK 1.8
 */
public interface LogService
{
    /**
     * [简要描述]:debug日志记录<br/>
     * [详细描述]:<br/>
     *
     * @param debugMsg : debug日志
     * @return void
     * llxiao  2018/9/2 - 16:57
     **/
    void debug(String debugMsg);

    /**
     * [简要描述]:info 日志记录<br/>
     * [详细描述]:<br/>
     *
     * @param message : 日志信息
     * @return void
     * llxiao  2018/9/2 - 16:55
     **/
    void info(String message);

    /**
     * [简要描述]:error 日志记录<br/>
     * [详细描述]:<br/>
     *
     * @param errorMsg : 错误消息
     * @param e : 异常
     * @return void
     * llxiao  2018/9/2 - 16:55
     **/
    void error(String errorMsg, Throwable e);

    /**
     * 警告日志
     *
     * @param message
     */
    void warn(String message);
}
