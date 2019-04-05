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
     * 在线
     */
    int ON_LINE = 0;
    /**
     * 离线
     */
    int OFF_LINE = 1;

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

    /**
     * [简要描述]:更新状态<br/>
     * [详细描述]:<br/>
     *
     * @param hostIp : 客户端IP
     * @param nettyPort : 客户端PORT
     * @param status : 0在线，1离线
     * llxiao  2019/4/1 - 10:34
     **/
    void updateStatus(String hostIp, int nettyPort, int status);

    /**
     * [简要描述]:更新应用的服务信息与NETTY连接的IP信息<br/>
     * [详细描述]:<br/>
     *
     * @param hostIp :
     * @param hostPort :
     * @param nettyPort :
     * @param nettyHostIp :
     * @return void
     * llxiao  2019/4/1 - 11:49
     **/
    void updateNettyInfo(String hostIp, int hostPort, int nettyPort, String nettyHostIp);
}
