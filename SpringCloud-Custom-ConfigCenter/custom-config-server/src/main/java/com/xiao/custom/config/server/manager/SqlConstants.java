package com.xiao.custom.config.server.manager;

/**
 * [简要描述]: SQL常量
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/1/29 16:53
 * @since JDK 1.8
 */
public interface SqlConstants
{

    /**
     * 应用名称和环境查找应用ID，返回Null则表示不存在
     */
    String SELECT_APPLICATION = "select id from t_client_application a where application = :application and profile = :profile";

    /**
     * 插入一条应用环境信息，应用名称+环境
     */
    String INSERT_APPLICATION = "INSERT INTO `t_client_application` (`application`,`profile`,`create_time`) VALUES (:application,:profile,now())";

    /**
     * 主键ID更新应服务状态
     */
    String UPDATE_APPLICATION = "update t_client_application set status = 0,update_time = now() where id = :id";

    /**
     * 应用ID+客户端IP+客户端提供服务IP 查询客户端信息主键ID
     */
    String SELECT_APPLICATION_HOST_INFO = "select id from t_client_host_info where client_application_id = :appId and host_ip = :ip and host_port = :port";

    /**
     * 插入一条应用服务端的客户端HOST信息 应用主键ID+客户端IP+客户端提供的服务端口
     */
    String INSERT_APPLICATION_HOST_INFO = "INSERT INTO `t_client_host_info` (`client_application_id`, `host_ip`, `host_port`,`create_time`) VALUES (:appId, :ip,:port, now())";

    /**
     * 主键ID更新用服务端的客户端HOST信息的状态
     */
    String UPDATE_APPLICATION_HOST_INFO = "update t_client_host_info set status = 0 ,update_time = now() where id = :id";

    /**
     * IP+PORT更新客户端状态
     */
    String UPDATE_CLIENT_STATUS = "update t_client_host_info set status = :status,update_time = now() where netty_ip = :nettyIp and netty_port = :nettyPort";

    /**
     * 绑定host和netty端口信息，标记上线
     */
    String UPDATE_NETTY_INFO = "update t_client_host_info set netty_port = :nettyPort,netty_ip = :nettyIp,status = 0,update_time = now()  where host_ip = :ip and host_port = :port";
}
