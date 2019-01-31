package com.xiao.custom.config.web.controller.app.vo;

import lombok.Data;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/12/21 20:53
 * @since JDK 1.8
 */
@Data
public class ApplicationVo
{
    private Long id;
    private String application;
    private String applicationName;
    private Long region;
    private String label;
    private String profile;
    private String confGroupIds;
}
