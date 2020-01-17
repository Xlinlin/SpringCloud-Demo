package com.xiao.custom.rest.starter.autoconfigure.log.service;

import com.xiao.custom.rest.starter.autoconfigure.log.dto.HttpRequestLog;

/**
 * [简要描述]: http 请求日志记录
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/11/29 15:33
 * @since JDK 1.8
 */
public interface HttpRequestLogService
{
    /**
     * [简要描述]:保存日志信息<br/>
     * [详细描述]:<br/>
     *
     * @param requestLog :
     * llxiao  2019/4/24 - 14:42
     **/
    void saveRequestLog(HttpRequestLog requestLog);
}
