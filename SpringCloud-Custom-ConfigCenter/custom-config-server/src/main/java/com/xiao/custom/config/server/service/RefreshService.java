package com.xiao.custom.config.server.service;

/**
 * [简要描述]: 刷新操作
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/4/4 09:36
 * @since JDK 1.8
 */
public interface RefreshService
{
    /**
     * [简要描述]:刷新服务<br/>
     * [详细描述]:<br/>
     *
     * @param ip :
     * @param port :
     * @return void
     * llxiao  2019/4/4 - 9:44
     **/
    boolean refresh(String ip, int port);
}
