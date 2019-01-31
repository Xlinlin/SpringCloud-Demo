package com.xiao.custom.config.simple.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/11/21 17:23
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/demo")
@RefreshScope
public class ControllerDemo
{
    @Value("${dymaic:test}")
    private String dbUrl;

    @RequestMapping("/getDbUrl")
    public String getDbUrl()
    {
        return dbUrl;
    }
}
