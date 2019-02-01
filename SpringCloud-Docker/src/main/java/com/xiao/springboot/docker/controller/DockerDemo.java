package com.xiao.springboot.docker.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/2/1 14:06
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/docker")
public class DockerDemo
{
    @RequestMapping("/hello")
    public String helloDocker()
    {
        return "Hello Docker!";
    }
}
