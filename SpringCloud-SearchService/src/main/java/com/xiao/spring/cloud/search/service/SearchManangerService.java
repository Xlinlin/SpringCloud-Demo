package com.xiao.spring.cloud.search.service;

import com.xiao.spring.cloud.search.dto.ElasticSearchDoc;

import java.util.List;

/**
 * [简要描述]: 搜索服务管理
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/10/8 09:58
 * @since JDK 1.8
 */
public interface SearchManangerService
{
    /**
     * [简要描述]:根据id查找doc<br/>
     * [详细描述]:<br/>
     *
     * @param id 索引ID
     * @param index 索引名称
     * @return 索引文档
     */
    ElasticSearchDoc getEsDocById(String id, String index);

    /**
     * [简要描述]:获取指定索引下所有文档<br/>
     * [详细描述]:<br/>
     *
     * @param index
     * @return 所有文档
     */
    List<ElasticSearchDoc> getAllDos(String index);

    /**
     * [简要描述]:根据defProdNo查找doc<br/>
     * [详细描述]:<br/>
     *
     * @param defProdNo 产品ID
     * @return 产品信息
     */
    ElasticSearchDoc getEsDocByDefProdNo(String defProdNo, String index);

    /**
     * [简要描述]:根据商品id查询出商品<br/>
     * [详细描述]:<br/>
     *
     * @param commoNo 商品ID
     * @return 商品信息
     */
    List<ElasticSearchDoc> getEsDocByCommoNo(String commoNo, String index);

    /**
     * [简要描述]:添加单个<br/>
     * [详细描述]:<br/>
     *
     * @param doc 文档
     * @return 添加状态
     */
    boolean addData(ElasticSearchDoc doc);

    /**
     * [简要描述]:批量添加数据<br/>
     * [详细描述]:
     *
     * @param docs 索引文档集
     * @return 更新结果状态
     */
    boolean addDatas(List<ElasticSearchDoc> docs);

    /**
     * [简要描述]:单个更新<br/>
     * [详细描述]:<br/>
     *
     * @param doc 索引文档
     * @return 更新状态
     */
    boolean updateData(ElasticSearchDoc doc);

    /**
     * [简要描述]:批量更新<br/>
     * [详细描述]:<br/>
     *
     * @param docs 索引文档集
     * @return 更新状态
     */
    boolean updateDatas(List<ElasticSearchDoc> docs);

    /**
     * [简要描述]:通过id修改单个文档<br/>
     * [详细描述]:<br/>
     *
     * @param doc 索引文档
     * @return 更新状态
     */
    boolean updateDataById(ElasticSearchDoc doc);

    /**
     * [简要描述]:根据默认货品编码修改<br/>
     * [详细描述]:<br/>
     *
     * @param doc 索引文档
     * @return 更新状态
     */
    boolean updateDataByDefProdNo(ElasticSearchDoc doc);

    /**
     * [简要描述]:批量更新库存状态
     * [详细描述]:<br/>
     *
     * @param allDocs 索引文档集
     * @return 更新状态
     */
    boolean updateHasStock(List<ElasticSearchDoc> allDocs);

    /**
     * [简要描述]:根据id单个删除<br/>
     * [详细描述]:<br/>
     *
     * @param id 索引ID
     * @param index 索引名称
     * @return 删除状态
     */
    boolean deleteData(String id, String index);

    /**
     * [简要描述]:ID批量删除<br/>
     * [详细描述]:<br/>
     *
     * @param docs 索引ID和索引集
     * @return 删除状态
     */
    boolean deleteDatas(List<ElasticSearchDoc> docs);

    /**
     * [简要描述]:根据商品编号删除<br/>
     * [详细描述]:<br/>
     *
     * @param commoNo 商品编号
     * @param index 索引名称
     * @return 删除状态
     */
    boolean deleteDatasByCommoNo(String commoNo, String index);
}
