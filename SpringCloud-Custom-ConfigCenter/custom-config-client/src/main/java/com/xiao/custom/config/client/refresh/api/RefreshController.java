package com.xiao.custom.config.client.refresh.api;

import com.xiao.custom.config.client.refresh.service.ConfigRefreshService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * [简要描述]: 配置刷新rest服务
 * [详细描述]:  待考虑安全问题
 *
 * @author llxiao
 * @version 1.0, 2019/1/29 14:18
 * @since JDK 1.8
 */
//@RestController
//@RequestMapping("/config")
public class RefreshController
{
    /**
     * 成功标识
     */
    private static final int SUCCESS = 0;

//    @Autowired
    private ConfigRefreshService configRefreshService;

    /**
     * [简要描述]:刷新配置<br/>
     * [详细描述]:仅支持post请求<br/>
     * <p>
     * llxiao  2019/1/29 - 14:23
     **/
    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    public int refresh()
    {
        configRefreshService.refresh();
        return SUCCESS;
    }
}
