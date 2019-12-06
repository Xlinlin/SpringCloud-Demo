package com.xiao.springcloud.rest.log.impl;

import com.alibaba.fastjson.JSONObject;
import com.xiao.springcloud.rest.stater.autoconfig.common.log.dto.HttpRequestLog;
import com.xiao.springcloud.rest.stater.autoconfig.common.log.service.HttpRequestLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * [简要描述]: HTTP 日志处理实现类
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/11/30 10:26
 * @since JDK 1.8
 */
@Service
@Slf4j
public class HttpLogServiceiImpl implements HttpRequestLogService
{
    /**
     * [简要描述]:保存日志信息<br/>
     * [详细描述]:<br/>
     *
     * @param requestLog :
     * llxiao  2019/4/24 - 14:42
     **/
    @Override
    public void saveRequestLog(HttpRequestLog requestLog)
    {
        log.info("Example log : {}", JSONObject.toJSONString(requestLog));
    }
}
