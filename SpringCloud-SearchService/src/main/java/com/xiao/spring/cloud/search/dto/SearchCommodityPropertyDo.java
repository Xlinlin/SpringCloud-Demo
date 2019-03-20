package com.xiao.spring.cloud.search.dto;

import lombok.Data;

import java.util.Set;

/**
 * [简要描述]: ES商品属性
 * [详细描述]:
 *
 * @author mjye
 * @version 1.0, 2019-02-23 14:45
 * @since JDK 1.8
 */
@Data
public class SearchCommodityPropertyDo
{
    //属性编码
    private String propertyNo;

    //属性名称(颜色、尺码等)
    private String propertyName;

    //属性选项
    private Set<SearchCommoPropOptionDto> setList;
}