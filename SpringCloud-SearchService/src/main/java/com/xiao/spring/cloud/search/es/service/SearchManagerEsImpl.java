package com.xiao.spring.cloud.search.es.service;

import com.alibaba.fastjson.JSON;
import com.xiao.springcloud.demo.common.exception.CommonException;
import com.xiao.spring.cloud.search.dto.ElasticSearchDoc;
import com.xiao.spring.cloud.search.es.client.ElasticSearchClient;
import com.xiao.spring.cloud.search.es.common.ESConstants;
import com.xiao.spring.cloud.search.es.common.SearchException;
import com.xiao.spring.cloud.search.service.SearchManangerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;

/**
 * [简要描述]: 搜索文档管理服务
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/10/8 13:57
 * @since JDK 1.8
 */
@Slf4j
@Service
public class SearchManagerEsImpl implements SearchManangerService, ESConstants
{
    /**
     * es客户端
     */
    @Autowired
    private ElasticSearchClient esClient;

    /**
     * [简要描述]:创建索引和默认mapping<br/>
     * [详细描述]:<br/>
     *
     * @param index : 索引名称
     * @return true成功
     * llxiao 2018/10/17 - 19:47
     **/
    @Override
    public boolean createIndexMapping(String index)
    {
        if (StringUtils.isBlank(index))
        {
            log.error("Create index and mapping error! index name:{}", index);
            return false;
        }
        if (this.existsIndex(index))
        {
            this.deleteIndex(index);
        }

        TransportClient client = esClient.getTransportClient();
        CreateIndexRequestBuilder cib = client.admin().indices().prepareCreate(index);
        try
        {
            XContentBuilder mapping = createMapping();
            cib.addMapping(index, mapping).execute().actionGet();
            return true;
        }
        catch (Exception e)
        {
            log.error("Create index and mapping error! index name:{}", index, e);
        }
        return false;
    }

    /**
     * 创建mapping
     *
     * @return
     * @exception IOException
     */
    private XContentBuilder createMapping() throws IOException
    {
        XContentBuilder mapping = XContentFactory.jsonBuilder().startObject().startObject("properties");
        mapping.startObject("id").field("type", "text").endObject();
        mapping.startObject("keyWords").field("type", "text").endObject();
        mapping.startObject("commodityNo").field("type", "keyword").endObject();
        mapping.startObject("commodityCode").field("type", "keyword").endObject();
        mapping.startObject("defProdNo").field("type", "keyword").endObject();
        mapping.startObject("defProdCode").field("type", "keyword").endObject();
        mapping.startObject("brandNo").field("type", "keyword").endObject();
        //fielddata 解决字段既可以聚合也可以分词查询问题
        mapping.startObject("brandName").field("type", "text").field("analyzer", "ik_max_word").field("fielddata", true)
                .startObject("fields").startObject("raw").field("type", "keyword").endObject().endObject().endObject();
        mapping.startObject("title").field("type", "text").field("analyzer", "ik_max_word").endObject();
        mapping.startObject("subTitle").field("type", "text").field("analyzer", "ik_max_word").endObject();
        mapping.startObject("extProps").field("type", "text").field("analyzer", "ik_max_word").endObject();
        mapping.startObject("skuProps").field("type", "text").field("analyzer", "ik_max_word").endObject();
        mapping.startObject("orgPrice").field("type", "double").endObject();
        mapping.startObject("salePrice").field("type", "double").endObject();
        mapping.startObject("productArea").field("type", "text").field("analyzer", "ik_max_word").endObject();
        mapping.startObject("oprtCatNo").field("type", "text").field("analyzer", "ik_max_word").endObject();
        mapping.startObject("oprtCatName").field("type", "text").field("analyzer", "ik_max_word").endObject();
        mapping.startObject("labels").field("type", "text").field("analyzer", "ik_max_word").endObject();
        mapping.startObject("saleTime").field("type", "date").endObject();
        mapping.startObject("picUrl").field("type", "keyword").endObject();
        mapping.startObject("stock").field("type", "integer").endObject();
        mapping.startObject("haitao").field("type", "integer").endObject();
        mapping.startObject("salesVolume").field("type", "long").endObject();
        mapping.startObject("commoCatNo").field("type", "keyword").endObject();
        mapping.startObject("newly").field("type", "integer").endObject();
        mapping.startObject("comments").field("type", "long").endObject();
        mapping.startObject("discountRate").field("type", "keyword").endObject();
        mapping.startObject("categoryName").field("type", "keyword").endObject();
        mapping.startObject("defSkuProp").field("type", "keyword").endObject();
        mapping.endObject().endObject();
        return mapping;
    }

    /**
     * [简要描述]:索引是否存在<br/>
     * [详细描述]:<br/>
     *
     * @param index : 索引名称
     * @return boolean
     * llxiao 2018/10/18 - 8:35
     **/
    @Override
    public boolean existsIndex(String index)
    {
        IndicesExistsRequest inExistsRequest = new IndicesExistsRequest(index);
        TransportClient client = esClient.getTransportClient();
        IndicesExistsResponse inExistsResponse = client.admin().indices().exists(inExistsRequest).actionGet();
        return inExistsResponse.isExists();
    }

    /**
     * [简要描述]:删除索引<br/>
     * [详细描述]:<br/>
     *
     * @param index : 索引名称
     * @return boolean
     * llxiao 2018/10/18 - 8:35
     **/
    @Override
    public boolean deleteIndex(String index)
    {
        boolean flag = false;
        if (StringUtils.isNotBlank(index))
        {
            DeleteIndexRequestBuilder deleteIndexRequestBuilder = esClient.getTransportClient().admin().indices()
                    .prepareDelete(index);
            if (null != deleteIndexRequestBuilder)
            {
                DeleteIndexResponse dResponse = deleteIndexRequestBuilder.execute().actionGet();
                flag = dResponse.isAcknowledged();
            }
        }
        return flag;
    }

    /**
     * [简要描述]:根据id查找doc<br/>
     * [详细描述]:<br/>
     *
     * @param id 索引ID
     * @param index 索引名称
     * @return 索引文档
     */
    @Override
    public ElasticSearchDoc getEsDocById(String id, String index)
    {
        if (StringUtils.isBlank(id) || StringUtils.isBlank(index))
        {
            throw new CommonException(SearchException.PARAM_IS_NULL.getCode(), "查询失败，索引名称和文档ID不能为空!");
        }
        TransportClient client = esClient.getTransportClient();
        GetRequestBuilder prepareGet = client.prepareGet(index, IDNEX_DEV_TYPE, id);
        Map<String, Object> map = prepareGet.get().getSourceAsMap();
        return JSON.parseObject(JSON.toJSONString(map), ElasticSearchDoc.class);
    }

    /**
     * [简要描述]:获取指定索引下的所有文档<br/>
     * [详细描述]:<br/>
     *
     * @param index 索引名称
     * @return 所有文档
     */
    @Override
    public List<ElasticSearchDoc> getAllDos(String index)
    {
        List<ElasticSearchDoc> esDocList = new ArrayList<>();
        TransportClient client = esClient.getTransportClient();
        long totalHits = client.prepareSearch(index).setTypes(IDNEX_DEV_TYPE).setQuery(QueryBuilders.matchAllQuery())
                .get().getHits().getTotalHits();
        int totalSize = (int) (totalHits / GET_ALL_SIZE) + 1;
        SearchResponse searchResponse;
        SearchHits hits;
        SearchHit[] searchHits;
        for (int i = 0; i < totalSize; i++)
        {
            searchResponse = client.prepareSearch(index).setTypes(IDNEX_DEV_TYPE)
                    .setQuery(QueryBuilders.matchAllQuery()).setSearchType(SearchType.QUERY_THEN_FETCH)
                    .setFrom(GET_ALL_SIZE * i).setSize(GET_ALL_SIZE).addSort(COMMON_NO, SortOrder.DESC).get();
            hits = searchResponse.getHits();
            searchHits = hits.getHits();
            for (SearchHit searchHit : searchHits)
            {
                esDocList.add(JSON.parseObject(searchHit.getSourceAsString(), ElasticSearchDoc.class));
            }
        }
        return esDocList;
    }

    /**
     * [简要描述]:根据defProdNo查找doc<br/>
     * [详细描述]:<br/>
     *
     * @param defProdNo 产品ID
     * @param index 索引名称
     * @return 产品信息
     */
    @Override
    public ElasticSearchDoc getEsDocByDefProdNo(String defProdNo, String index)
    {
        if (StringUtils.isBlank(defProdNo) || StringUtils.isBlank(index))
        {
            throw new CommonException(SearchException.PARAM_IS_NULL.getCode(), "查询失败，索引名称和货品编号不能为空!");
        }
        List<ElasticSearchDoc> esDocList = new ArrayList<>();
        TransportClient client = esClient.getTransportClient();
        SearchRequestBuilder searchBuilder = client.prepareSearch(index);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        QueryBuilder accurateQuery = QueryBuilders.matchQuery(ESConstants.DEFPRODNO, defProdNo);
        searchBuilder.setQuery(queryBuilder.must(accurateQuery));
        SearchResponse response = searchBuilder.execute().actionGet();
        SearchHits hits = response.getHits();
        String searchSource;
        for (SearchHit searchHit : hits)
        {
            searchSource = searchHit.getSourceAsString();
            esDocList.add(JSON.parseObject(searchSource, ElasticSearchDoc.class));
        }
        if (esDocList.size() == 1)
        {
            return esDocList.get(0);
        }
        return new ElasticSearchDoc();
    }

    /**
     * [简要描述]:根据商品编号查询出商品<br/>
     * [详细描述]:<br/>
     *
     * @param commoNo 商品ID
     * @param index 索引名称
     * @return 商品信息
     */
    @Override
    public List<ElasticSearchDoc> getEsDocByCommoNo(String commoNo, String index)
    {
        List<ElasticSearchDoc> esDocList = new ArrayList<>();
        if (StringUtils.isBlank(commoNo) || StringUtils.isBlank(index))
        {
            throw new CommonException(SearchException.PARAM_IS_NULL.getCode(), "查询失败，索引名称和商品编号不能为空!");
        }
        TransportClient client = esClient.getTransportClient();
        SearchRequestBuilder searchBuilder = client.prepareSearch(index);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        QueryBuilder accurateQuery = QueryBuilders.matchQuery(ESConstants.COMMON_NO, commoNo);

        searchBuilder.setQuery(queryBuilder.must(accurateQuery));

        SearchResponse response = searchBuilder.execute().actionGet();
        SearchHits hits = response.getHits();
        String searchSource;
        for (SearchHit searchHit : hits)
        {
            searchSource = searchHit.getSourceAsString();
            esDocList.add(JSON.parseObject(searchSource, ElasticSearchDoc.class));
        }
        return esDocList;
    }

    /**
     * [简要描述]:添加单个<br/>
     * [详细描述]:<br/>
     *
     * @param doc 文档
     * @return 添加状态
     */
    @Override
    public boolean addData(ElasticSearchDoc doc)
    {
        if (doc == null || StringUtils.isBlank(doc.getId()) || StringUtils.isBlank(doc.getIndex()))
        {
            throw new CommonException(SearchException.PARAM_IS_NULL.getCode(), "添加失败，索引名称和文档ID不能为空!");
        }
        TransportClient client = esClient.getTransportClient();
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        IndexRequestBuilder irb = client.prepareIndex(doc.getIndex(), IDNEX_DEV_TYPE, doc.getId());
        irb.setSource(JSON.parseObject(JSON.toJSONString(doc), Map.class));
        bulkRequest.add(irb);
        BulkResponse bulkResponse = bulkRequest.get();
        if (bulkResponse.hasFailures())
        {
            log.error(bulkResponse.getTook() + "");
            return false;
        }
        return true;
    }

    /**
     * [简要描述]:批量添加数据<br/>
     * [详细描述]:
     *
     * @param docs 索引文档集
     * @return 更新结果状态
     */
    @Override
    public boolean addDatas(List<ElasticSearchDoc> docs)
    {
        if (CollectionUtils.isEmpty(docs))
        {
            log.error("Parameter  is null  in ManagerService addDatas() method");
            return false;
        }
        TransportClient client = esClient.getTransportClient();
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        String index;
        IndexRequestBuilder irb;
        for (ElasticSearchDoc esDoc : docs)
        {
            index = esDoc.getIndex();
            if (StringUtils.isBlank(index) || StringUtils.isBlank(esDoc.getId()))
            {
                continue;
            }
            irb = client.prepareIndex(index, IDNEX_DEV_TYPE, esDoc.getId());
            irb.setSource(JSON.parseObject(JSON.toJSONString(esDoc), Map.class));
            bulkRequest.add(irb);
        }
        BulkResponse bulkResponse = bulkRequest.get();
        if (bulkResponse.hasFailures())
        {
            log.error(bulkResponse.getTook() + "");
            return false;
        }
        return true;
    }

    /**
     * [简要描述]:ID单个更新<br/>
     * [详细描述]:<br/>
     *
     * @param doc 索引文档
     * @return 更新状态
     */
    @Override
    public boolean updateData(ElasticSearchDoc doc)
    {
        if (doc == null || StringUtils.isBlank(doc.getId()) || StringUtils.isBlank(doc.getIndex()))
        {
            throw new CommonException(SearchException.PARAM_IS_NULL.getCode(), "更新失败，索引名称和文档ID不能为空!");
        }
        TransportClient client = esClient.getTransportClient();
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        UpdateRequestBuilder urb = client.prepareUpdate(doc.getIndex(), IDNEX_DEV_TYPE, doc.getId());

        urb.setDoc(JSON.parseObject(JSON.toJSONString(doc), Map.class));
        bulkRequest.add(urb);

        BulkResponse bulkResponse = bulkRequest.get();
        if (bulkResponse.hasFailures())
        {
            log.error(bulkResponse.getTook() + "");
        }
        return true;
    }

    /**
     * [简要描述]:批量更新<br/>
     * [详细描述]:<br/>
     *
     * @param docs 索引文档集
     * @return 更新状态
     */
    @Override
    public boolean updateDatas(List<ElasticSearchDoc> docs)
    {
        if (CollectionUtils.isEmpty(docs))
        {
            throw new CommonException(SearchException.PARAM_IS_NULL.getCode(), "批量添加失败，商品不能为空");
        }
        TransportClient client = esClient.getTransportClient();
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        UpdateRequestBuilder urb;
        String index;
        for (ElasticSearchDoc esDoc : docs)
        {
            index = esDoc.getIndex();
            if (StringUtils.isBlank(index) || StringUtils.isBlank(esDoc.getId()))
            {
                continue;
            }
            urb = client.prepareUpdate(index, IDNEX_DEV_TYPE, esDoc.getId());
            urb.setDoc(JSON.parseObject(JSON.toJSONString(esDoc), Map.class));
            bulkRequest.add(urb);
        }
        BulkResponse bulkResponse = bulkRequest.get();
        if (bulkResponse.hasFailures())
        {
            log.error(bulkResponse.getTook() + "");
        }
        return true;
    }

    /**
     * [简要描述]:通过id修改单个文档<br/>
     * [详细描述]:<br/>
     *
     * @param doc 索引文档
     * @return 更新状态
     */
    @Override
    public boolean updateDataById(ElasticSearchDoc doc)
    {
        if (null == doc || StringUtils.isBlank(doc.getId()) || StringUtils.isBlank(doc.getIndex()))
        {
            throw new CommonException(SearchException.PARAM_IS_NULL.getCode(), "更新失败，索引名称和文档ID不能为空!");
        }
        TransportClient client = esClient.getTransportClient();
        if (null != client)
        {
            ElasticSearchDoc esDocById = getEsDocById(doc.getId(), doc.getIndex());
            Map<String, Object> docMap = JSON.parseObject(JSON.toJSONString(doc), Map.class);
            Map<String, Object> oldDoc = JSON.parseObject(JSON.toJSONString(esDocById), Map.class);
            if (docMap.containsKey(ESConstants.SALES_TAG_NAME))
            {
                // 标签更新处理
                updateTagNames(oldDoc, docMap);
            }
            else
            {
                oldDoc.putAll(docMap);
            }
            BulkRequestBuilder bulkRequest = client.prepareBulk();
            UpdateRequestBuilder urb = client.prepareUpdate(doc.getIndex(), IDNEX_DEV_TYPE, doc.getId());

            urb.setDoc(oldDoc);
            bulkRequest.add(urb);

            BulkResponse bulkResponse = bulkRequest.get();
            if (bulkResponse.hasFailures())
            {
                log.error(bulkResponse.getTook() + "");
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * [简要描述]:根据默认货品编码修改<br/>
     * [详细描述]:<br/>
     *
     * @param doc 索引文档
     * @return 更新状态
     */
    @Override
    public boolean updateDataByDefProdNo(ElasticSearchDoc doc)
    {
        if (null == doc || StringUtils.isBlank(doc.getId()) || StringUtils.isBlank(doc.getIndex()))
        {
            throw new CommonException(SearchException.PARAM_IS_NULL.getCode(), "更新失败，索引名称和文档ID不能为空!");
        }
        ElasticSearchDoc esDocById = getEsDocByDefProdNo(doc.getId(), doc.getIndex());
        if (null == esDocById)
        {
            throw new CommonException(SearchException.NOT_FOUND_DOC.getCode(), "更新失败，更新的文档不存在!");
        }
        Map<String, Object> oldMap = JSON.parseObject(JSON.toJSONString(esDocById), Map.class);
        oldMap.putAll(JSON.parseObject(JSON.toJSONString(doc), Map.class));
        String keyId = doc.getId();
        TransportClient client = esClient.getTransportClient();
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        UpdateRequestBuilder urb = client.prepareUpdate(doc.getIndex(), IDNEX_DEV_TYPE, keyId);
        urb.setDoc(oldMap);
        bulkRequest.add(urb);

        BulkResponse bulkResponse = bulkRequest.get();
        if (bulkResponse.hasFailures())
        {
            log.error(bulkResponse.getTook() + "");
            return false;
        }
        return true;
    }

    /**
     * [简要描述]:批量更新库存状态
     * [详细描述]:<br/>
     *
     * @param allDocs 索引文档集
     * @return 更新状态
     */
    @Override
    public boolean updateHasStock(List<ElasticSearchDoc> allDocs)
    {
        if (CollectionUtils.isEmpty(allDocs))
        {
            throw new CommonException(SearchException.PARAM_IS_NULL.getCode(), "批量更新库存失败，索引名称和文档ID不能为空!");
        }
        TransportClient client = esClient.getTransportClient();
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        UpdateRequestBuilder urb;
        for (ElasticSearchDoc doc : allDocs)
        {
            urb = client.prepareUpdate(doc.getIndex(), IDNEX_DEV_TYPE, doc.getId());
            urb.setDoc(doc);
            bulkRequest.add(urb);
        }

        BulkResponse bulkResponse = bulkRequest.get();
        if (bulkResponse.hasFailures())
        {
            log.error(bulkResponse.getTook() + "");
        }
        return true;
    }

    /**
     * [简要描述]:根据id单个删除<br/>
     * [详细描述]:<br/>
     *
     * @param id 索引ID
     * @param index 索引名称
     * @return 删除状态
     */
    @Override
    public boolean deleteData(String id, String index)
    {
        if (StringUtils.isBlank(id) || StringUtils.isBlank(index))
        {
            throw new CommonException(SearchException.PARAM_IS_NULL.getCode(), "删除失败，索引名称和文档ID不能为空!");
        }
        TransportClient client = esClient.getTransportClient();
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        DeleteRequestBuilder drb = client.prepareDelete(index, IDNEX_DEV_TYPE, id);
        bulkRequest.add(drb);
        BulkResponse bulkResponse = bulkRequest.get();
        if (bulkResponse.hasFailures())
        {
            log.error(bulkResponse.getTook() + "fail Id:" + id);
        }
        return true;
    }

    /**
     * [简要描述]:批量删除<br/>
     * [详细描述]:<br/>
     *
     * @param docs 索引ID和索引名称集
     * @return 删除状态
     */
    @Override
    public boolean deleteDatas(List<ElasticSearchDoc> docs)
    {
        if (CollectionUtils.isEmpty(docs))
        {
            log.error("Parameter  is null  in ManagerService deleteDatas() method");
            return false;
        }
        TransportClient client = esClient.getTransportClient();
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        DeleteRequestBuilder drb;
        for (ElasticSearchDoc doc : docs)
        {
            drb = client.prepareDelete(doc.getIndex(), IDNEX_DEV_TYPE, doc.getId());
            bulkRequest.add(drb);
        }
        BulkResponse bulkResponse = bulkRequest.get();
        if (bulkResponse.hasFailures())
        {
            log.error(bulkResponse.getTook() + "");
            return false;
        }
        return true;
    }

    /**
     * [简要描述]:根据商品编号删除<br/>
     * [详细描述]:<br/>
     *
     * @param commoNo 商品编号
     * @param index 索引名称
     * @return 删除状态
     */
    @Override
    public boolean deleteDatasByCommoNo(String commoNo, String index)
    {
        return false;
    }

    private void updateTagNames(Map<String, Object> esDocById, Map<String, Object> docMap)
    {
        String desTags = (String) docMap.remove(ESConstants.SALES_TAG_NAME);
        if (StringUtils.isNotBlank(desTags))
        {
            List<String> desTagNames = JSON.parseArray(desTags, String.class);
            Set<String> dts = new HashSet<>(desTagNames);
            esDocById.put(ESConstants.SALES_TAG_NAME, JSON.toJSONString(dts));
        }
        else
        {
            esDocById.put(ESConstants.SALES_TAG_NAME, "");
        }
        esDocById.putAll(docMap);
    }
}
