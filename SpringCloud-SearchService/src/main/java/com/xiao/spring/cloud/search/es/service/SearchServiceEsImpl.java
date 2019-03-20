package com.xiao.spring.cloud.search.es.service;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.xiao.spring.cloud.search.dto.*;
import com.xiao.spring.cloud.search.es.client.ElasticSearchClient;
import com.xiao.spring.cloud.search.es.common.AnalyzeType;
import com.xiao.spring.cloud.search.es.common.ESConstants;
import com.xiao.spring.cloud.search.es.common.OrderField;
import com.xiao.spring.cloud.search.es.log.ISearchLogService;
import com.xiao.spring.cloud.search.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.WeightBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * [简要描述]: 搜索服务ES实现
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/10/8 11:01
 * @since JDK 1.8
 */
@Service
@Slf4j
public class SearchServiceEsImpl implements SearchService, ESConstants
{

    /**
     * 日志记录器
     */
    private static final Log LOG = LogFactory.getLog(SearchServiceEsImpl.class);

    @Autowired
    private ElasticSearchClient esClient;

    @Autowired
    private ISearchLogService searchLogService;

    @Value("${elasticsearch.highlight.pretags}")
    private String preTags;

    @Value("${elasticsearch.highlight.posttags}")
    private String postTags;

    @Value("${elasticsearch.total}")
    private int searchTotal;

    /**
     * [简要描述]: 因子的权重默认值
     * [详细描述]: 当后台对因子未设置权重时，该因子的权重设置为默认值0
     **/
    private static int weight = 0;

    /**
     * 日志返回结果最大长度取值
     */
    private static final int RESULT_MAX_LENGTH = 5;

    /**
     * 分析器 0ES默认分词器，1IK中文分词器
     */
    @Value("${elasticsearch.analyze.type}")
    private int analyzeType = 0;

    /**
     * [简要描述]:搜索<br/>
     * [详细描述]:<br/>
     *
     * @param searchRequestDo :
     * @return PaginationDo
     * llxiao  2018/10/8 - 11:07
     **/
    @Override
    public SearchCommodityResultDo search(SearchRequestDo searchRequestDo)
    {
        if (LOG.isDebugEnabled() && null != searchRequestDo)
        {
            LOG.debug("Start search data for ES with search params:" + searchRequestDo);
        }
        // 对空值置为null

        LocalDateTime startTime = LocalDateTime.now();
        SearchLogDo searchLog = new SearchLogDo();
        searchLog.setStartTime(startTime);

        PaginationDo page = new PaginationDo();
        TransportClient client = esClient.getTransportClient();
        if (null != searchRequestDo && StringUtils.isNotBlank(searchRequestDo.getIndex()) && null != client)
        {
            // 处理查询
            processSearch(searchRequestDo, page, client);
        }
        else
        {
            // 防止调用者空指针
            page.setResults(new ArrayList<>());
            LOG.error("搜索失败，请求参数及索引及EsClient不能为空!");
        }
        processSearchLog(startTime, searchLog, page);

        return this.paginationDo2CommdityList(page);
    }

    @Override
    @Cacheable(value = "searchMenu", key = "'searchMenu'.concat({#searchRequestDo.index}).concat({#searchRequestDo.keyWords}).concat({#searchRequestDo.oprtCatNo})")
    public SearchMenusDo searchMenu(SearchRequestDo searchRequestDo)
    {
        if (LOG.isDebugEnabled() && null != searchRequestDo)
        {
            LOG.debug("Start search data for ES with search params:" + searchRequestDo);
        }

        SearchMenusDo menu = new SearchMenusDo();
        TransportClient client = esClient.getTransportClient();
        if (null != searchRequestDo && StringUtils.isNotBlank(searchRequestDo.getIndex()) && null != client)
        {
            // 处理查询
            processMenu(searchRequestDo, menu, client);
        }
        else
        {
            LOG.error("搜索失败，请求参数及索引及EsClient不能为空!");
        }
        return menu;
    }

    @Override
    public long commodityTotal(SearchRequestDo searchRequestDo)
    {
        if (LOG.isDebugEnabled() && null != searchRequestDo)
        {
            LOG.debug("Start search data for ES with search params:" + searchRequestDo);
        }
        LocalDateTime startTime = LocalDateTime.now();
        SearchLogDo searchLog = new SearchLogDo();
        searchLog.setStartTime(startTime);

        TransportClient client = esClient.getTransportClient();
        Long commodityTotal;
        // 对空值置为null
        if (null != searchRequestDo && StringUtils.isNotBlank(searchRequestDo.getIndex()) && null != client)
        {
            // 处理查询
            commodityTotal = processCommodityTotal(searchRequestDo, client);
        }
        else
        {
            commodityTotal = 0L;
            LOG.error("搜索失败，请求参数及索引及EsClient不能为空!");
        }
        processSearchLog(startTime, searchLog, null);

        return commodityTotal;
    }

    private SearchCommodityResultDo paginationDo2CommdityList(PaginationDo page)
    {
        SearchCommodityResultDo searchCommodityResultDo = null;
        List<ElasticSearchDoc> commodityList;
        if (null != page)
        {
            commodityList = new ArrayList<>();
            searchCommodityResultDo = new SearchCommodityResultDo();
            searchCommodityResultDo.setPageNo(page.getPageNo());
            searchCommodityResultDo.setPageSize(page.getPageSize());
            searchCommodityResultDo.setTotal(page.getTotal());
            for (SearchResultDo result : page.getResults())
            {
                result.getDoc().setExtProps("");
                result.getDoc().setSkuProps("");
                commodityList.add(result.getDoc());
            }
            searchCommodityResultDo.setCommodityList(commodityList);
        }

        searchCommodityResultDo.setPageTotal(
                (searchCommodityResultDo.getTotal() / searchCommodityResultDo.getPageSize()) + CONSTANT_ONE);
        return searchCommodityResultDo;
    }

    private void processSearchLog(LocalDateTime startTime, SearchLogDo searchLog, PaginationDo page)
    {
        try
        {
            // 日志数据处理
            LocalDateTime endTime = LocalDateTime.now();
            searchLog.setEndTime(endTime);
            searchLog.setResult(resultProcess(page).toString());
            // 搜索消耗的时间
            Duration duration = Duration.between(startTime, endTime);
            searchLog.setCostTime(duration.toMillis());
            // 日志处理
            saveLog(searchLog);
        }
        catch (Exception e)
        {
            // 日志保存处理异常 不能影响到主流程的查询
            LOG.error("Save log error!", e);
        }
    }

    /**
     * [简要描述]:搜索日志处理<br/>
     * [详细描述]:<br/>
     *
     * @param searchLog
     */
    private void saveLog(SearchLogDo searchLog)
    {
        if (LOG.isDebugEnabled())
        {
            LOG.debug("Search log info:" + searchLog.toString());
        }
        searchLogService.addSearchLog(searchLog);
    }

    /**
     * [简要描述]:返回结果处理<br/>
     * [详细描述]:<br/>
     *
     * @param page
     * @return
     */
    private PaginationDo resultProcess(PaginationDo page)
    {
        PaginationDo pd = new PaginationDo();
        pd.setPageNo(page.getPageNo());
        pd.setPageSize(page.getPageSize());
        pd.setTook(page.getTook());
        pd.setTotal(page.getTotal());
        List<SearchResultDo> result = page.getResults();

        if (null != result && result.size() > RESULT_MAX_LENGTH)
        {
            pd.setResults(result.subList(0, RESULT_MAX_LENGTH));
        }
        else
        {
            pd.setResults(result);
        }
        return pd;
    }

    /**
     * [简要描述]: 处理聚和菜单
     * [详细描述]:<br/>
     *
     * @return void
     * mjye  2019-02-28 - 16:45
     **/
    private void processMenu(SearchRequestDo request, SearchMenusDo menu, TransportClient client)
    {
        String index = request.getIndex();
        SearchRequestBuilder searchBuilder = client.prepareSearch(index);
        // 设置是否按查询匹配度排序
        searchBuilder.setExplain(true);

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //权重
        QueryBuilder functionScoreQueryBuilder = this.getFunctionScoreQueryBuilder(request);
        boolQuery.must(functionScoreQueryBuilder);
        // 设置过滤条件
        setFilters(request, boolQuery);
        searchBuilder.setFetchSource(new String[] { ES_EXT_PROPS, ES_SKU_PROPS }, null);

        // 设置菜单的品牌、价格、分类
        this.setMenuInBrandCategorySale(request, boolQuery, menu);

        // 设置查询
        searchBuilder.setQuery(boolQuery);
        // 设置排序
        setOrder(searchBuilder, request);
        // 执行搜索
        SearchResponse response = searchBuilder.execute().actionGet();
        SearchHits hits = response.getHits();
        long took = response.getTook().getMillis();
        // 普通查询
        this.processSearchMenu(hits, menu);
        menu.setTook(took);
        if (LOG.isDebugEnabled())
        {
            LOG.debug("Search result:" + menu);
        }
    }

    /**
     * [简要描述]:查询<br/>
     * [详细描述]:<br/>
     *
     * @param request 查询请求
     * @param page 分页组装数据
     * 搜索日志
     * @param client esClient
     */
    private void processSearch(SearchRequestDo request, PaginationDo page, TransportClient client)
    {
        Integer pageNo = request.getPageNo();
        Integer pageSize = request.getPageSize();
        pageNo = null == pageNo ? 1 : pageNo;
        page.setPageNo(pageNo);
        pageSize = null == pageSize ? 1 : pageSize;
        pageSize = pageSize > MAX_PAGE_SIZE ? MAX_PAGE_SIZE : pageSize;
        page.setPageSize(pageSize);

        String index = request.getIndex();
        SearchRequestBuilder searchBuilder = client.prepareSearch(index);
        // 设置是否按查询匹配度排序
        searchBuilder.setExplain(true);
        // 分页信息
        searchBuilder.setFrom((pageNo - 1) * pageSize).setSize(pageSize);

        //        FunctionScoreQueryBuilder functionScoreQueryBuilder = this.getFunctionScoreQueryBuilder(request);

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //权重
        QueryBuilder functionScoreQueryBuilder = this.getFunctionScoreQueryBuilder(request);
        boolQuery.must(functionScoreQueryBuilder);
        // 设置过滤条件
        setFilters(request, boolQuery);
        // 设置查询
        searchBuilder.setQuery(boolQuery);

        // 设置高亮
        searchBuilder.highlighter(setHighlightBuilder());
        // 设置排序
        setOrder(searchBuilder, request);

        // 执行搜索
        SearchResponse response = searchBuilder.execute().actionGet();
        SearchHits hits = response.getHits();
        long took = response.getTook().getMillis();
        long total = hits.getTotalHits();
        // 普通查询
        processSearchResult(page, hits, total);
        page.setTook(took);
        if (LOG.isDebugEnabled())
        {
            LOG.debug("Search result:" + page);
        }
    }

    /**
     * [简要描述]: 获取商品总数
     * [详细描述]:<br/>
     *
     * @return int
     * mjye  2019-03-05 - 14:42
     **/
    private long processCommodityTotal(SearchRequestDo request, TransportClient client)
    {
        String index = request.getIndex();
        SearchRequestBuilder searchBuilder = client.prepareSearch(index);
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //权重
        QueryBuilder functionScoreQueryBuilder = this.getFunctionScoreQueryBuilder(request);
        boolQuery.must(functionScoreQueryBuilder);
        // 设置过滤条件
        setFilters(request, boolQuery);
        // 设置查询
        searchBuilder.setQuery(boolQuery);

        // 执行搜索
        SearchResponse response = searchBuilder.execute().actionGet();
        SearchHits hits = response.getHits();
        return hits.getTotalHits();

    }

    /**
     * [简要描述]: 设置菜单的品牌名称，分类，价格
     * [详细描述]: 聚合<br/>
     *
     * @return void
     * mjye  2019-02-26 - 9:58
     **/
    private void setMenuInBrandCategorySale(SearchRequestDo request, QueryBuilder query, SearchMenusDo menus)
    {
        // 品牌、价格、分类聚合
        List<String> brandlist = aggregationFunction(request.getIndex(), query, ES_BRAND_NO);
        List<String> pricelist = aggregationFunction(request.getIndex(), query, SALE_PRICE);
        List<String> catelist = aggregationFunction(request.getIndex(), query, ES_CATEGORY_NAME);
        this.getThreeOprtCat(catelist, menus);

    }

    /**
     * [简要描述]: 对品牌，分类，价格区间进行聚和
     * [详细描述]:<br/>
     *
     * @param index : 索引
     * @param query : 查询条件
     * @param column : 聚合字段
     * @return java.util.List<java.lang.String>
     * mjye  2019-02-28 - 15:29
     **/
    private List<String> aggregationFunction(String index, QueryBuilder query, String column)
    {
        TransportClient client = esClient.getTransportClient();
        SearchRequestBuilder searchBuilder = client.prepareSearch(index);

        if (query == null)
        {
            query = QueryBuilders.matchAllQuery();
        }

        //        if ("brandName".equals(column))
        //        {
        //            column = column + ".raw";
        //        }
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms(column).field(column)
                .order(BucketOrder.key(true)).size((1 << 31) - 1);
        searchBuilder.setQuery(query).addAggregation(termsAggregationBuilder);

        SearchResponse response = searchBuilder.execute().actionGet();

        Terms terms = response.getAggregations().get(column);

        List<String> list = new ArrayList<>();
        for (Terms.Bucket bucket : terms.getBuckets())
        {
            list.add(bucket.getKey().toString());
        }
        return list;
    }

    /**
     * [简要描述]:分词查询<br/>
     * [详细描述]:<br/>
     *
     * @param request 查询请求
     * @param page 响应数据
     * @param client ES客户端
     * @param searchBuilder 查询builder
     * @return took
     */
    private long segmentQuery(SearchRequestDo request, PaginationDo page, TransportClient client,
            SearchRequestBuilder searchBuilder, SearchMenusDo menus)
    {

        // IK_SMART-标准分词 IK_MAX_WORD-最大分词 STANDARD-ES默认分词全拆
        // IK_SMART拆词
        List<String> openKeywords = this.openedWords(AnalyzeType.IK_SMART, client, request.getKeyWords());

        // 拆词查询
        searchBuilder.setQuery(setQueryBuilder(openKeywords, request));

        SearchResponse response = searchBuilder.execute().actionGet();
        SearchHits hits = response.getHits();
        long total = hits.getTotalHits();
        long took = response.getTook().getMillis();
        if (total > 0 && total >= searchTotal)
        {
            // 数据处理
            processSearchResult(page, hits, total);
        }
        else
        {
            // IK MAX WORD拆词
            openKeywords = this.openedWords(AnalyzeType.IK_MAX_WORD, client, request.getKeyWords());
            // 拆词查询
            searchBuilder.setQuery(setQueryBuilder(openKeywords, request));

            response = searchBuilder.execute().actionGet();
            hits = response.getHits();
            total = hits.getTotalHits();
            took = response.getTook().getMillis();
            if (total > 0 && total >= searchTotal)
            {
                // 普通拆词查询
                processSearchResult(page, hits, total);
            }
            else if (STANDARD == this.analyzeType)
            {
                // ES默认拆词，全拆
                openKeywords = this.openedWords(AnalyzeType.STANDARD, client, request.getKeyWords());
                // 拆词查询
                searchBuilder.setQuery(setQueryBuilder(openKeywords, request));

                response = searchBuilder.execute().actionGet();
                hits = response.getHits();
                total = hits.getTotalHits();
                took = response.getTook().getMillis();
                if (total > 0)
                {
                    // 处理结果
                    processSearchResult(page, hits, total);
                }
            }
        }

        return took;
    }

    /**
     * [简要描述]:设置排序<br/>
     * [详细描述]:<br/>
     *
     * @param searchBuilder SearchRequestBuilder
     * @param request SearchRequestDo
     */
    private void setOrder(SearchRequestBuilder searchBuilder, SearchRequestDo request)
    {
        int orderType = request.getSortFeild();

        SortOrder sortOrder = SortOrder.ASC;
        int sort = request.getSort();
        if (DESC_SORT == sort)
        {
            sortOrder = SortOrder.DESC;
        }

        if (OrderField.PRICE.getType() == orderType)
        {
            // 价格
            searchBuilder.addSort(SALE_PRICE, sortOrder);
        }
        else if (OrderField.SALES.getType() == orderType)
        {
            // 销量
            searchBuilder.addSort(SALES_VOLUME, sortOrder);
        }
        else if (OrderField.SLAESTIME.getType() == orderType)
        {
            // 上架时间
            searchBuilder.addSort(SALE_TIME, sortOrder);
        }
        else if (OrderField.COMMENTS.getType() == orderType)
        {
            // 好评度
            searchBuilder.addSort(COMMENTS, sortOrder);
        }

    }

    /**
     * [简要描述]:分词查询<br/>
     * [详细描述]:<br/>
     *
     * @param analyze 分词器类型
     * @param client ES客户端
     * @param content 分词内容
     * @return 拆分后的词
     */
    private List<String> openedWords(AnalyzeType analyze, TransportClient client, String content)
    {
        List<String> allWorlds = new ArrayList<>();
        if (StringUtils.isNotBlank(content))
        {
            // 指定分词器
            AnalyzeResponse response = client.admin().indices().prepareAnalyze(content).setAnalyzer(analyze.getType())
                    .execute().actionGet();
            List<AnalyzeResponse.AnalyzeToken> tokens = response.getTokens();
            if (LOG.isDebugEnabled())
            {
                LOG.debug("ElasticSearch excute analyzeToken result:" + JSON.toJSONString(tokens));
            }
            for (AnalyzeResponse.AnalyzeToken token : tokens)
            {
                allWorlds.add(token.getTerm());
            }
        }

        return allWorlds;
    }

    /**
     * [简要描述]:设置高亮信息<br/>
     * [详细描述]:<br/>
     *
     * @param searchResultDo SearchResultDo
     * @param searchHit SearchHit
     */
    private void setHighlight(SearchResultDo searchResultDo, SearchHit searchHit)
    {
        Text[] text;
        Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
        if (highlightFields.containsKey(ES_TITLE_FEILD))
        {
            text = highlightFields.get(ES_TITLE_FEILD).getFragments();
            if (text.length > 0)
            {
                searchResultDo.setHighlightTitle(text[0].string());
            }
        }
        if (highlightFields.containsKey(ES_SUBTITLE_FEILD))
        {
            text = highlightFields.get(ES_SUBTITLE_FEILD).getFragments();
            if (text.length > 0)
            {
                searchResultDo.setHighlightSubTitle(text[0].string());
            }
        }
    }

    /**
     * [简要描述]: 处理聚和菜单查询
     * [详细描述]:<br/>
     *
     * @return void
     * mjye  2019-02-28 - 16:57
     **/
    private void processSearchMenu(SearchHits hits, SearchMenusDo menu)
    {
        // 数据结果
        List<SearchResultDo> searchResponses = new ArrayList<>();
        List<String> searchStrs = new ArrayList<>();
        SearchResultDo searchResultDo;
        String searchSource;
        ElasticSearchDoc esd;
        // 迭代查询结果
        for (SearchHit searchHit : hits)
        {
            searchResultDo = new SearchResultDo();
            searchSource = searchHit.getSourceAsString();
            if (StringUtils.isNotBlank(searchSource))
            {
                searchStrs.add(searchSource);
                esd = JSON.parseObject(searchSource, ElasticSearchDoc.class);
                esd.setId(searchHit.getId());
                searchResultDo.setDoc(esd);
                searchResponses.add(searchResultDo);
            }
        }
        if (LOG.isDebugEnabled())
        {
            LOG.debug("Search resource:" + searchStrs);
        }
        this.getMenus(searchResponses, menu);
    }

    /**
     * [简要描述]:查询结果处理<br/>
     * [详细描述]:<br/>
     *
     * @param page 分页数据
     * @param hits 查询数据
     * @param total 查询总数
     */
    private void processSearchResult(PaginationDo page, SearchHits hits, long total)
    {
        page.setTotal((int) total);
        // 数据结果
        List<SearchResultDo> searchResponses = new ArrayList<>();
        List<String> searchStrs = new ArrayList<>();
        SearchResultDo searchResultDo;
        String searchSource;
        ElasticSearchDoc esd;
        // 迭代查询结果
        for (SearchHit searchHit : hits)
        {
            searchResultDo = new SearchResultDo();
            searchSource = searchHit.getSourceAsString();
            if (StringUtils.isNotBlank(searchSource))
            {
                searchStrs.add(searchSource);
                esd = JSON.parseObject(searchSource, ElasticSearchDoc.class);
                esd.setId(searchHit.getId());
                searchResultDo.setDoc(esd);
                // 高亮字段
                setHighlight(searchResultDo, searchHit);
                searchResponses.add(searchResultDo);
            }
        }
        if (LOG.isDebugEnabled())
        {
            LOG.debug("Search resource:" + searchStrs);
        }
        page.setResults(searchResponses);
    }

    /**
     * [简要描述]:设置高亮信息<br/>
     * [详细描述]:<br/>
     *
     * @return 高亮huilder
     */
    private HighlightBuilder setHighlightBuilder()
    {
        HighlightBuilder highlightBuilder = new HighlightBuilder().field("*").requireFieldMatch(false);
        highlightBuilder.preTags(preTags);
        highlightBuilder.postTags(postTags);
        //        highlightBuilder.field("subTitle");
        //        highlightBuilder.field("title");
        return highlightBuilder;
    }

    /**
     * [简要描述]:设置拆词查询条件<br/>
     * [详细描述]:<br/>
     *
     * @param openKeywords 拆词后的关键词
     * @param request 查询条件
     * @return QueryBuilder
     */
    private QueryBuilder setQueryBuilder(List<String> openKeywords, SearchRequestDo request)
    {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (CollectionUtil.isNotEmpty(openKeywords))
        {
            QueryBuilder multiMatch;
            for (String word : openKeywords)
            {
                // 关键字 标签 中多字段查询
                multiMatch = QueryBuilders.multiMatchQuery(word, ESConstants.ES_KEY_WORDS, ESConstants.SALES_TAG_NAME)
                        .type(MultiMatchQueryBuilder.Type.PHRASE_PREFIX).slop(1).tieBreaker(0.3f);
                queryBuilder.should(multiMatch);
            }
            setFilters(request, queryBuilder);
            return queryBuilder;
        }
        return setQueryBuilder(request);
    }

    /**
     * [简要描述]:设置查询条件<br/>
     * [详细描述]:<br/>
     *
     * @param request 搜索请求
     * @return QueryBuilder
     */
    private QueryBuilder setQueryBuilder(SearchRequestDo request)
    {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        String keyWords = request.getKeyWords();
        // 匹配多个字段
        if (StringUtils.isNotBlank(keyWords))
        {
            // 关键字和标签匹配查询
            QueryBuilder multiMatch = QueryBuilders
                    .multiMatchQuery(keyWords, ES_BRAND_NO, ES_COMMODITY_NO, ES_TITLE, ES_SUB_TITLE, ES_OPRT_CAT_NAME, ES_LABELS, ES_EXT_PROPS, ES_SKU_PROPS, ES_PRODUCT_AREA)
                    .type(MultiMatchQueryBuilder.Type.PHRASE_PREFIX);
            queryBuilder.should(multiMatch);
        }
        // 设置过滤条件
        setFilters(request, queryBuilder);

        return queryBuilder;
    }

    /**
     * [简要描述]:设置过滤查询<br/>
     * [详细描述]:<br/>
     *
     * @param request 搜索请求
     * @param queryBuilder BoolQueryBuilder
     */
    private void setFilters(SearchRequestDo request, BoolQueryBuilder queryBuilder)
    {
        // 新品不为-1，即有过滤选择
        if (INVALID_FLAG != request.getNewly())
        {
            // 新品过滤
            queryBuilder.must(QueryBuilders.matchQuery(NEWLY, request.getNewly()));
        }

        // 分类编号过滤
        // 仅对从分类入口进入商品列表是有效
        if (StringUtils.isBlank(request.getKeyWords()) && StringUtils.isNotBlank(request.getOprtCatNo()))
        {
            queryBuilder.must(QueryBuilders.matchQuery(ES_CATEGORY_NO, request.getOprtCatNo()));
        }

        // 是否有货过滤
        if (INVALID_FLAG != request.getHasStock())
        {
            // 无货
            queryBuilder.must(QueryBuilders.matchQuery(HAS_STOCK, request.getHasStock()));
        }

        // 商品分类过滤查询
        if (CollectionUtil.isNotEmpty(request.getCategoryNo()))
        {
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            for (String s : request.getCategoryNo())
            {
                boolQueryBuilder.should(QueryBuilders.matchQuery(ES_CATEGORY_NO, s));
            }
            queryBuilder.must(boolQueryBuilder);
        }

        // 商品品牌过滤查询
        if (CollectionUtil.isNotEmpty(request.getBrandNo()))
        {
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            for (String s : request.getBrandNo())
            {
                boolQueryBuilder.should(QueryBuilders.matchQuery(ES_BRAND_NO, s));
            }
            queryBuilder.must(boolQueryBuilder);
        }

        // sku规格属性过滤
        if (CollectionUtil.isNotEmpty(request.getSkuPropsNo()))
        {
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            for (String s : request.getSkuPropsNo())
            {
                boolQueryBuilder.should(QueryBuilders.matchQuery(ES_SKU_PROPS, s));
            }
            queryBuilder.must(boolQueryBuilder);
        }

        // 商品扩展属性过滤
        if (CollectionUtil.isNotEmpty(request.getExtPropsNo()))
        {
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            for (String s : request.getExtPropsNo())
            {
                boolQueryBuilder.should(QueryBuilders.matchQuery(ES_EXT_PROPS, s));
            }
            queryBuilder.must(boolQueryBuilder);
        }

        // 价格区间过滤
        if (CollectionUtil.isNotEmpty(request.getRangesPrices()))
        {
            BoolQueryBuilder priceBoolQuery = QueryBuilders.boolQuery();
            for (String rangesPrice : request.getRangesPrices())
            {
                String[] split = rangesPrice.trim().split("-");
                Double minPrice = Double.parseDouble(split[CONSTANT_ZERO]);
                Double maxPrice = Double.parseDouble(split[split.length - CONSTANT_ONE]);
                if (CONSTANT_TWO == split.length && minPrice < maxPrice)
                {
                    priceBoolQuery.should(QueryBuilders.rangeQuery(ESConstants.SALE_PRICE).from(minPrice).to(maxPrice));
                }
            }
            queryBuilder.must(priceBoolQuery);
        }
    }

    /**
     * [简要描述]: 根绝查询到的结果聚合出筛选菜单
     * [详细描述]:<br/>
     *
     * @param searchResponses : 查询到的结果
     * @return com.purcotton.omni.search.dto.SearchMenusDo
     * mjye  2019-02-23 - 11:12
     **/
    private SearchMenusDo getMenus(List<SearchResultDo> searchResponses, SearchMenusDo menus)
    {
        if (CollectionUtil.isNotEmpty(searchResponses))
        {

            Set<String> brandName = new HashSet<>();
            Set<String> oprtCatName = new HashSet<>();
            Set<SearchCommodityPropertyDo> skuProps = new HashSet<>();
            Set<SearchCommodityPropertyDo> extProps = new HashSet();
            Set<ShopPriceRangeDto> price = new HashSet();
            // 业务逻辑代码聚合部分
            menus.setSkuProps(skuProps);
            menus.setExtProps(extProps);
        }
        return menus;
    }

    /**
     * [简要描述]: 获取商品的三级运营分类
     * [详细描述]:<br/>
     *
     * @param catelist : 商品的运营分类
     * @return java.lang.String
     * mjye  2019-02-23 - 12:00
     **/
    private void getThreeOprtCat(List<String> catelist, SearchMenusDo menus)
    {
        Set<SearchCategoryDo> threeOprtCat = null;
        if (CollectionUtil.isNotEmpty(catelist))
        {
            threeOprtCat = new HashSet<>();
            SearchCategoryDo searchCategoryDo;
            for (String cate : catelist)
            {
                List<String> strings1 = Arrays.asList(cate.split(","));
                for (String s : strings1)
                {
                    searchCategoryDo = new SearchCategoryDo();
                    String[] split = s.split("-");
                    searchCategoryDo.setCategoryName(split[CONSTANT_ZERO]);
                    searchCategoryDo.setCategoryNo(split[split.length - CONSTANT_ONE]);
                    threeOprtCat.add(searchCategoryDo);
                }
            }
        }
        menus.setOprtCatName(threeOprtCat);
    }

    /**
     * [简要描述]: 根据权重因子查询
     * [详细描述]:<br/>
     *
     * @return org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder
     * mjye  2019-02-23 - 18:14
     **/
    private QueryBuilder getFunctionScoreQueryBuilder(SearchRequestDo request)
    {
        //        List<SearchShopWeightDto> searchShopWeightDtos = shopRestService.queryShopWeight(shopWeightDto);
        // 业务查询权重配置
        List<SearchShopWeightDto> searchShopWeightDtos = new ArrayList<>();
        String searchContent = request.getKeyWords();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        FunctionScoreQueryBuilder.FilterFunctionBuilder[] filterFunctionBuilders = new FunctionScoreQueryBuilder.FilterFunctionBuilder[0];

        // 匹配多个字段
        if (StringUtils.isNotBlank(request.getKeyWords()))
        {
            // 关键字和标签匹配查询
            QueryBuilder multiMatch = QueryBuilders
                    .multiMatchQuery(searchContent, ES_TITLE, ES_BRAND_NAME, ES_COMMODITY_NO, ES_SUB_TITLE, ES_OPRT_CAT_NAME, ES_LABELS, ES_EXT_PROPS, ES_SKU_PROPS, ES_PRODUCT_AREA)
                    .type(MultiMatchQueryBuilder.Type.PHRASE_PREFIX);
            queryBuilder.should(multiMatch);

            if (CollectionUtil.isNotEmpty(searchShopWeightDtos))
            {
                filterFunctionBuilders = new FunctionScoreQueryBuilder.FilterFunctionBuilder[9];
                for (int i = 0; i < searchShopWeightDtos.size(); i++)
                {
                    queryBuilder.should(QueryBuilders
                            .matchQuery(searchShopWeightDtos.get(i).getEnglishName(), searchContent));
                    if (ESConstants.CONSTANT_ZERO <= searchShopWeightDtos.get(i).getWeightFactor())
                    {
                        weight = searchShopWeightDtos.get(i).getWeightFactor();
                    }
                    filterFunctionBuilders[i] = new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders
                            .termQuery(searchShopWeightDtos.get(i).getEnglishName(), searchContent), new WeightBuilder()
                            .setWeight(weight));
                }
                return QueryBuilders.functionScoreQuery(queryBuilder, filterFunctionBuilders)
                        .scoreMode(FunctionScoreQuery.ScoreMode.SUM).boostMode(CombineFunction.MULTIPLY);
            }
        }
        return queryBuilder;
    }
}
