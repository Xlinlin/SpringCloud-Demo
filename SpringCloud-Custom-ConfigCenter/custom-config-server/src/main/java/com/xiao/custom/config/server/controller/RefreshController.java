package com.xiao.custom.config.server.controller;

import com.xiao.custom.config.server.service.RefreshService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/4/4 10:29
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/refresh")
public class RefreshController
{
    @Autowired
    private RefreshService refreshService;

    @RequestMapping("/client")
    public boolean refresh(@RequestParam("ip") String ip, @RequestParam("port") int port)
    {
        return refreshService.refresh(ip, port);
    }
}
