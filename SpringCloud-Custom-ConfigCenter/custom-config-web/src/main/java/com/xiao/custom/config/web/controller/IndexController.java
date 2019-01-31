package com.xiao.custom.config.web.controller;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author zhdong
 * @version 1.0,  2018/11/21
 * @since JDK 1.8
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 首页转发
 * @author zhdong
 * Date 2018/8/23
 */
@Controller
public class IndexController
{

    @Value("${omni.channel.admin.page.index:index/index}")
    private String indexPage;

    @RequestMapping(value = "/")
    public String index(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        return indexPage;
    }

    @RequestMapping(value = "/index")
    public String index2(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        return indexPage;
    }


}
