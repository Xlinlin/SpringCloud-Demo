package com.xiao.custom.config.client.refresh.service;

/**
 * [简要描述]: 配置刷新服务
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/1/29 14:19
 * @since JDK 1.8
 */
public interface ConfigRefreshService
{
    /**
     * [简要描述]:刷新spring容器中的变更的配置<br/>
     * [详细描述]:<br/>
     * <p>
     * llxiao  2019/1/29 - 14:19
     **/
    void refresh();
}
