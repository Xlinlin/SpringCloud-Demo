package com.xiao.custom.config.service.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * [简要描述]: 刷新客户端，调用配置中心的API
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/4/4 11:10
 * @since JDK 1.8
 */
@FeignClient(value = "winner-config-server")
@RequestMapping("/refresh")
public interface RefreshFeign
{
    @RequestMapping("/client")
    boolean refresh(@RequestParam("ip") String ip, @RequestParam("port") int port);
}
