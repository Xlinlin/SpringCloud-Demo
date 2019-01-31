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
}
