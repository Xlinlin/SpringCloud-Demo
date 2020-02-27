package com.xiao.springcloud.demo.common.sign.service;

/**
 * [简要描述]: 第三方App权限管理
 * [详细描述]:
 *
 * @author xiaolinlin
 * @version 1.0, 2020/2/21 09:23
 * @since JDK 1.8
 */
public interface AppManagerService
{
    /**
     * [简要描述]:appId是否存在<br/>
     * [详细描述]:<br/>
     *
     * @param appId :
     * @return boolean
     * xiaolinlin  2020/2/21 - 9:24
     **/
    boolean exist(String appId);

    /**
     * [简要描述]:appId获取对应的appKey<br/>
     * [详细描述]:<br/>
     *
     * @param appId :
     * @return java.lang.String
     * xiaolinlin  2020/2/21 - 9:24
     **/
    String getAppKey(String appId);

    /**
     * [简要描述]:添加一个app应用信息<br/>
     * [详细描述]:<br/>
     *
     * @param appId :
     * @param appKey :
     * @return boolean
     * xiaolinlin  2020/2/21 - 9:25
     **/
    boolean addApp(String appId, String appKey);
}
