package com.xiao.spring.cloud.search.dto;

import lombok.Data;

/**
 * [简要描述]: 分类
 * [详细描述]: 聚合菜单使用
 *
 * @author mjye
 * @version 1.0, 2019-03-02 14:29
 * @since JDK 1.8
 */
@Data
public class SearchCategoryDo
{
    private String categoryName;

    private String categoryNo;
}