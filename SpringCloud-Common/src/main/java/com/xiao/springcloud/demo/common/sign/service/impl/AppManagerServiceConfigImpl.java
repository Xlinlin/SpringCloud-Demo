package com.xiao.springcloud.demo.common.sign.service.impl;

import omni.purcotton.omni.inface.center.common.sign.service.AppManagerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author xiaolinlin
 * @version 1.0, 2020/2/21 09:27
 * @since JDK 1.8
 */
@Service
public class AppManagerServiceConfigImpl implements AppManagerService, InitializingBean
{
    /**
     * 本地内存APP info实现
     */
    private ConcurrentHashMap<String, String> appInfoMap = new ConcurrentHashMap<>();

    /**
     * appInfo配置实现<br>
     * 格式：appId:appKey,appId2:appKey2
     */
    @Value("${interface.auth.app.info:1001:abcdefg111}")
    private String appInfo;

    /**
     * [简要描述]:appId是否存在<br/>
     * [详细描述]:<br/>
     *
     * @param appId :
     * @return boolean
     * xiaolinlin  2020/2/21 - 9:24
     **/
    @Override
    public boolean exist(String appId)
    {
        return appInfoMap.containsKey(appId);
    }

    /**
     * [简要描述]:appId获取对应的appKey<br/>
     * [详细描述]:<br/>
     *
     * @param appId :
     * @return java.lang.String
     * xiaolinlin  2020/2/21 - 9:24
     **/
    @Override
    public String getAppKey(String appId)
    {
        return appInfoMap.get(appId);
    }

    /**
     * [简要描述]:添加一个app应用信息<br/>
     * [详细描述]:<br/>
     *
     * @param appId :
     * @param appKey :
     * @return boolean
     * xiaolinlin  2020/2/21 - 9:25
     **/
    @Override
    public boolean addApp(String appId, String appKey)
    {
        appInfoMap.put(appKey, appKey);
        return true;
    }

    /**
     * Invoked by a BeanFactory after it has set all bean properties supplied
     * (and satisfied BeanFactoryAware and ApplicationContextAware).
     * <p>This method allows the bean instance to perform initialization only
     * possible when all bean properties have been set and to throw an
     * exception in the event of misconfiguration.
     *
     * @exception Exception in the event of misconfiguration (such
     * as failure to set an essential property) or if initialization fails.
     */
    @Override
    public void afterPropertiesSet() throws Exception
    {
        if (StringUtils.isNotBlank(appInfo))
        {
            // 初始化appiId KV配置
            String[] appInfos = appInfo.split(",");
            for (String app : appInfos)
            {
                String[] appKv = app.split(":");
                if (appKv.length == 2)
                {
                    appInfoMap.put(appKv[0], appKv[1]);
                }
            }
        }
    }
}
