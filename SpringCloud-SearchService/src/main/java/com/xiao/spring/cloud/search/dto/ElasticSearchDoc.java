/*
 * Winner
 * 文件名  :ElasticSearchDoc.java
 * 创建人  :llxiao
 * 创建时间:2018年1月11日
 */

package com.xiao.spring.cloud.search.dto;

import lombok.Data;

import java.util.Date;

/**
 * [简要描述]:Elastic search 存储文档<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年1月11日
 * @since Purcotton-Search B01
 */
@Data
public class ElasticSearchDoc
{
    /**
     * 文档id
     */
    private String id;

    /**
     * 关键字
     */
    private String keyWords;

    /**
     * 商品编号
     */
    private String commoNo;

    /**
     * 默认货品编码
     */
    private String defProdNo;

    /**
     * 图片信息
     */
    private String prodPicUrl;

    /**
     * 商品分类编号
     */
    private String commoCatNo;

    /**
     * 商品运营分类编号（多个用逗号隔开）
     */
    private String oprtCatNo;

    /**
     * 标题,商品标题，SKU名称
     */
    private String title;

    /**
     * 子标题，活动商品名称
     */
    private String subTitle;

    /**
     * 价格
     */
    private float price;

    /**
     * 上架时间
     */
    private Date listTime;

    /**
     * 新品 1新品，0默认
     */
    private int newly = 0;

    /**
     * 有无库存,1无，0有，默认0
     */
    private int hasStock = 0;

    /**
     * 销量
     */
    private int salesVolume;

    /**
     * 评论数量
     */
    private int comments;

    /**
     * 活动属性 折扣信息
     */
    private String discountRate;

    /**
     * 标签集合
     */
    private String salesTagName;

    /**
     * 索引名称
     */
    private String index;
}
