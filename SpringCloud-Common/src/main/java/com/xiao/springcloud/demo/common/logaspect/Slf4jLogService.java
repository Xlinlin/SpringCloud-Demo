package com.xiao.springcloud.demo.common.logaspect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * [简要描述]: slf4j日志记录
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/9/2 16:58
 * @since JDK 1.8
 */
@Service
@Slf4j
public class Slf4jLogService implements LogService
{
    /**
     * [简要描述]:debug日志记录<br/>
     * [详细描述]:<br/>
     *
     * @param debugMsg : debug日志
     * llxiao  2018/9/2 - 16:57
     **/
    @Override
    public void debug(String debugMsg)
    {
        if (log.isDebugEnabled())
        {
            log.debug(debugMsg);
        }
    }

    /**
     * [简要描述]:info 日志记录<br/>
     * [详细描述]:<br/>
     *
     * @param message : 日志信息
     * llxiao  2018/9/2 - 16:55
     **/
    @Override
    public void info(String message)
    {
        if (log.isInfoEnabled())
        {
            log.info(message);
        }
    }

    /**
     * 警告日志
     *
     * @param message
     */
    @Override
    public void warn(String message)
    {
        log.warn(message);
    }

    /**
     * [简要描述]:error 日志记录<br/>
     * [详细描述]:<br/>
     *
     * @param errorMsg : 错误消息
     * @param e : 异常
     * llxiao  2018/9/2 - 16:55
     **/
    @Override
    public void error(String errorMsg, Throwable e)
    {
        log.error(errorMsg, e);
    }
}
