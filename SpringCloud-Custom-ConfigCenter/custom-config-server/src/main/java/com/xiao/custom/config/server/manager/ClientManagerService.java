package com.xiao.custom.config.server.manager;

/**
 * [简要描述]: 服务端链接管理
 * [详细描述]: 服务名称+链接的IP地址
 *
 * @author llxiao
 * @version 1.0, 2019/1/28 11:26
 * @since JDK 1.8
 */
public interface ClientManagerService
{
    /**
     * [简要描述]:存储服务链接信息，服务IP<br/>
     * [详细描述]:<br/>
     *
     * @param serviceName : 应用名
     * @param profile: 应用环境
     * @param hostIp : 应用对应服务的IP
     * @param hostPort : 应用对应服务的端口
     * llxiao  2019/1/28 - 11:38
     **/
    void setClientHost(String serviceName, String profile, String hostIp, int hostPort);
}
