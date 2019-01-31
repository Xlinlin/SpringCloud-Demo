package com.xiao.custom.config.server.service;

import java.util.Map;

/**
 * [简要描述]: 资源服务
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/11/23 08:36
 * @since JDK 1.8
 */
public interface RepositoryService
{
    /**
     * [简要描述]:<br/>
     * [详细描述]:<br/>
     *
     * @param ip : 服务IP
     * @param application : 应用名称
     * @param label : 标签默认master
     * @param profile : 环境
     * @return java.util.Map
     * llxiao  2018/11/23 - 8:38
     **/
    Map<String, Object> getPropertySource(String ip, String application, String label, String profile);
}
