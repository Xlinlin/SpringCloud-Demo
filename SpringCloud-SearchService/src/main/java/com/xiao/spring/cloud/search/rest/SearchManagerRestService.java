package com.xiao.spring.cloud.search.rest;

import com.xiao.springcloud.demo.common.logaspect.LogAnnotation;
import com.xiao.spring.cloud.search.dto.ElasticSearchDoc;
import com.xiao.spring.cloud.search.service.SearchManangerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * [简要描述]: 搜索服务数据管理
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/10/8 16:22
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/search/manager")
@Slf4j
public class SearchManagerRestService
{
    /*
     * 搜索管理服务
     */
    @Autowired
    private SearchManangerService searchManangerService;

    /**
     * [简要描述]:根据id查找doc<br/>
     * [详细描述]:<br/>
     *
     * @param id 索引ID
     * @param index 索引名称
     * @return 索引文档
     */
    @RequestMapping("/getById")
    @LogAnnotation
    public ElasticSearchDoc getEsDocById(String id, String index)
    {
        return searchManangerService.getEsDocById(id, index);
    }

    /**
     * [简要描述]:获取指定索引下所有文档<br/>
     * [详细描述]:<br/>
     *
     * @param index 索引名称
     * @return 所有文档
     */
    @RequestMapping("/getAll")
    @LogAnnotation
    public List<ElasticSearchDoc> getAllDos(String index)
    {
        return searchManangerService.getAllDos(index);
    }

    /**
     * [简要描述]:根据defProdNo查找doc<br/>
     * [详细描述]:<br/>
     *
     * @param defProdNo 产品ID
     * @return 产品信息
     */
    @RequestMapping("/getByProdNo")
    @LogAnnotation
    public ElasticSearchDoc getEsDocByDefProdNo(String defProdNo, String index)
    {
        return searchManangerService.getEsDocByDefProdNo(defProdNo, index);
    }

    /**
     * [简要描述]:根据商品id查询出商品<br/>
     * [详细描述]:<br/>
     *
     * @param commoNo 商品ID
     * @return 商品信息
     */
    @RequestMapping("/getByCommodityNo")
    public List<ElasticSearchDoc> getEsDocByCommoNo(String commoNo, String index)
    {
        return searchManangerService.getEsDocByCommoNo(commoNo, index);
    }

    /**
     * [简要描述]:添加单个<br/>
     * [详细描述]:<br/>
     *
     * @param doc 文档
     * @return 添加状态
     */
    @RequestMapping("/addData")
    @LogAnnotation
    public boolean addData(@RequestBody ElasticSearchDoc doc)
    {
        return this.searchManangerService.addData(doc);
    }

    /**
     * [简要描述]:批量添加数据<br/>
     * [详细描述]:
     *
     * @param docs 索引文档集
     * @return 更新结果状态
     */
    @RequestMapping("/batchAdd")
    @LogAnnotation
    public boolean addDatas(@RequestBody List<ElasticSearchDoc> docs)
    {
        return this.searchManangerService.addDatas(docs);
    }

    /**
     * [简要描述]:单个更新<br/>
     * [详细描述]:<br/>
     *
     * @param doc 索引文档
     * @return 更新状态
     */
    @RequestMapping("/update")
    @LogAnnotation
    public boolean updateData(ElasticSearchDoc doc)
    {
        return this.searchManangerService.updateData(doc);
    }

    /**
     * [简要描述]:批量更新<br/>
     * [详细描述]:<br/>
     *
     * @param docs 索引文档集
     * @return 更新状态
     */
    @RequestMapping("/batchUpdate")
    @LogAnnotation
    public boolean updateDatas(List<ElasticSearchDoc> docs)
    {
        return this.searchManangerService.updateDatas(docs);
    }

    /**
     * [简要描述]:通过id修改单个文档<br/>
     * [详细描述]:<br/>
     *
     * @param doc 索引文档
     * @return 更新状态
     */
    @RequestMapping("/updateById")
    @LogAnnotation
    public boolean updateDataById(ElasticSearchDoc doc)
    {
        return this.searchManangerService.updateDataById(doc);
    }

    /**
     * [简要描述]:批量更新库存状态
     * [详细描述]:<br/>
     *
     * @param docs 索引文档集
     * @return 更新状态
     */
    @RequestMapping("/batchUpdateStock")
    @LogAnnotation()
    public boolean updateHasStock(List<ElasticSearchDoc> docs)
    {
        return this.searchManangerService.updateHasStock(docs);
    }

    /**
     * [简要描述]:根据id单个删除<br/>
     * [详细描述]:<br/>
     *
     * @param id 索引ID
     * @param index 索引名称
     * @return 删除状态
     */
    @RequestMapping("/deleteById")
    @LogAnnotation
    public boolean deleteData(String id, String index)
    {
        return this.searchManangerService.deleteData(id, index);
    }

    /**
     * [简要描述]:ID批量删除<br/>
     * [详细描述]:<br/>
     *
     * @param docs 索引ID和索引集，主要包含id和Index
     * @return 删除状态
     */
    @RequestMapping("/batchDeleteById")
    public boolean deleteDatas(List<ElasticSearchDoc> docs)
    {
        return this.searchManangerService.deleteDatas(docs);
    }

    /**
     * [简要描述]:根据商品编号删除<br/>
     * [详细描述]:<br/>
     *
     * @param commoNo 商品编号
     * @param index 索引名称
     * @return 删除状态
     */
    @RequestMapping("/deleteByCommoNo")
    @LogAnnotation
    public boolean deleteDataByCommoNo(String commoNo, String index)
    {
        return this.searchManangerService.deleteDatasByCommoNo(commoNo, index);
    }
}
