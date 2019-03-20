package com.xiao.spring.cloud.search.dto;

import lombok.Data;

/**
 * [简要描述]: ES品牌
 * [详细描述]:
 *
 * @author mjye
 * @version 1.0, 2019-02-28 15:06
 * @since JDK 1.8
 */
@Data
public class SearchBrandDo
{
    /**
     * 品牌编号
     */
    private String brandNo;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 品牌图片URL
     */
    private String brandUrl;
}