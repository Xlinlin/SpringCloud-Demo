package com.xiao.custom.config.client.refresh.service.impl;

import com.xiao.custom.config.client.refresh.service.ConfigRefreshService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.stereotype.Service;

/**
 * [简要描述]: 配置刷新服务
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/1/29 14:20
 * @since JDK 1.8
 */
//@Service
@Slf4j
public class ConfigRefreshServiceImpl implements ConfigRefreshService
{
//    @Autowired
    private ContextRefresher refresher;

    /**
     * [简要描述]:刷新spring容器中的变更的配置<br/>
     * [详细描述]:<br/>
     * <p>
     * llxiao  2019/1/29 - 14:19
     **/
    @Override
    public void refresh()
    {
        if (log.isDebugEnabled())
        {
            log.debug("开始执行配置刷新动作......................");
        }
        refresher.refresh();
        if (log.isDebugEnabled())
        {
            log.debug("配置刷新完成......................");
        }
    }
}
