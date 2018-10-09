/*
 * Winner 
 * 文件名  :ESConstants.java
 * 创建人  :llxiao
 * 创建时间:2018年1月11日
*/

package com.xiao.spring.cloud.search.es.common;

/**
 * [简要描述]:ES常量池<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年1月11日
 * @since Purcotton-Search B01
 */
public interface ESConstants
{
    /**
     * ES type默认值
     */
    String ES_PURCOTTON_TYPE = "purcotton";

    /**
     * 集群配置名称
     */
    String ES_CLUSTER_NAME_CONF = "cluster.name";

    /**
     * 自动嗅探配置
     */
    String ES_SNIFF_CONF = "client.transport.sniff";

    /**
     * 分页最大数量 100
     */
    int MAX_PAGE_SIZE = 100;

    /**
     * 获取所有的数量,最大值
     */
    int GET_ALL_SIZE = 2000;
    /**
     * 新品
     */
    int NEWLY_PRODUCT = 1;

    /**
     * 普通
     */
    int NORMAL_PRODUCT = 0;

    /**
     * 有货
     */
    int HAVE_STOCK = 0;

    /**
     * 无货
     */
    int NOT_HAVE_STOCK = 1;

    /**
     * 无效，不处理标志
     */
    int INVALID_FLAG = -1;

    /**
     * IK maxword分词器
     */
    int IK_ANALYZE_TYPE = 1;

    /**
     * ES 默认分词器
     */
    int STANDARD = 0;
    /**
     * 将序排序
     */
    int DESC_SORT = 1;

    /****************************** ES 字段常量 start **************************/

    /**
     * 标题字段
     */
    String ES_TITLE_FEILD = "title";

    /**
     * 子标题字段
     */
    String ES_SUBTITLE_FEILD = "subTitle";

    /**
     * 运营类型
     */
    String ES_OPRTCATNO_FEILD = "oprtCatNo";

    /**
     * 索引关键字
     */
    String ES_KEY_WORDS = "keyWords";

    /**
     * 商品编号
     */
    String COMMON_NO = "commoNo";

    /**
     * 默认货品编码
     */
    String DEFPRODNO = "defProdNo";

    /**
     * 库存过滤字段
     */
    String HAS_STOCK = "hasStock";

    /**
     * 新品字段
     */
    String NEWLY = "newly";

    /**
     * 价格字段
     */
    String PRICE = "price";

    /**
     * 销量字段
     */
    String SALES_VOLUME = "salesVolume";

    /**
     * 上架时间字段
     */
    String LIST_TIME = "listTime";

    /**
     * 评论数字段
     */
    String COMMENTS = "comments";

    /**
     * 销售标签名称
     */
    String SALES_TAG_NAME = "salesTagName";

    /****************************** ES 字段常量 end **************************/

    /**
     * 是否使用ES
     * "on"表示使用ES
     * "off"表示使用solr
     */
    String USE_ELASTIC_SEARCH = "useElasticSearch";

    /**
     * 分词阈值 搜索到指定值不进行拆词
     */
    String WORD_SEGMENTATION_THRESHOLD = "thresholdTotal";
    
    /**
     * ES热词 缓存key
     */
    String ES_IK_HOT_WORDS_KEY = "esIKHotWords";

    /**
     * 关闭elasticsearch 使用solr
     */
    String OFF_ELASTIC_SEARCH = "off";

    /**
     * 开启elasticsearch
     */
    String ON_ELASTIC_SEARCH = "on";
}
