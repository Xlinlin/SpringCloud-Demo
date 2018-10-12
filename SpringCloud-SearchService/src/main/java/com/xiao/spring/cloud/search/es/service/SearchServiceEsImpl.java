package com.xiao.spring.cloud.search.es.service;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.xiao.spring.cloud.search.dto.ElasticSearchDoc;
import com.xiao.spring.cloud.search.dto.PaginationDo;
import com.xiao.spring.cloud.search.dto.SearchRequestDo;
import com.xiao.spring.cloud.search.dto.SearchResultDo;
import com.xiao.spring.cloud.search.es.client.ElasticSearchClient;
import com.xiao.spring.cloud.search.es.common.AnalyzeType;
import com.xiao.spring.cloud.search.es.common.ESConstants;
import com.xiao.spring.cloud.search.es.common.OrderField;
import com.xiao.spring.cloud.search.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private ElasticSearchClient esClient;

    @Value("${elastic.search.highlight.pretags}")
    private String preTags;

    @Value("${elastic.search.highlight.posttags}")
    private String postTags;

    @Value("${elastic.search.total}")
    private int searchTotal;

    /**
     * 分析器 0ES默认分词器，1IK中文分词器
     */
    @Value("${elastic.search.analyze.type}")
    private int analyzeType = 0;

    /**
     * [简要描述]:搜索<br/>
     * [详细描述]:<br/>
     *
     * @param searchRequestDo :
     * @return com.purcotton.search.dto.PaginationDo
     * llxiao  2018/10/8 - 11:07
     **/
    @Override
    public PaginationDo search(SearchRequestDo searchRequestDo)
    {
        if (log.isDebugEnabled() && null != searchRequestDo)
        {
            log.debug("Start search data for ES with search params:" + searchRequestDo);
        }
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
            log.error("搜索失败，请求参数及索引及EsClient不能为空!");
        }
        return page;
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
        // 设置查询
        searchBuilder.setQuery(setQueryBuilder(request));
        // 设置高亮
        searchBuilder.highlighter(setHighlightBuilder());
        // 设置排序
        setOrder(searchBuilder, request);

        // 执行搜索
        SearchResponse response = searchBuilder.execute().actionGet();
        SearchHits hits = response.getHits();
        long took = response.getTook().getMillis();
        long total = hits.getTotalHits();
        if (total > 0 && total >= searchTotal)
        {
            // 普通查询
            processSearchResult(page, hits, total);
        }
        else
        {
            // 拆词查询
            took = segmentQuery(request, page, client, searchBuilder);
        }
        page.setTook(took);
        if (log.isDebugEnabled())
        {
            log.debug("Search result:" + page);
        }
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
            SearchRequestBuilder searchBuilder)
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
            searchBuilder.addSort(PRICE, sortOrder);
        }
        else if (OrderField.SALES.getType() == orderType)
        {
            // 销量
            searchBuilder.addSort(SALES_VOLUME, sortOrder);
        }
        else if (OrderField.SLAESTIME.getType() == orderType)
        {
            // 上架时间
            searchBuilder.addSort(LIST_TIME, sortOrder);
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
            if (log.isDebugEnabled())
            {
                log.debug("ElasticSearch excute analyzeToken result:" + JSON.toJSONString(tokens));
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
        if (log.isDebugEnabled())
        {
            log.debug("Search resource:" + searchStrs);
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
                    .multiMatchQuery(keyWords, ESConstants.ES_KEY_WORDS, ESConstants.SALES_TAG_NAME)
                    .type(MultiMatchQueryBuilder.Type.PHRASE_PREFIX);
            queryBuilder.should(multiMatch);
        }

        if (request.getMinPrice() > 0)
        {
            // 价格区间只有最小值
            RangeQueryBuilder rangequerybuilder = QueryBuilders.rangeQuery(ESConstants.PRICE)
                    .from(request.getMinPrice()).to(request.getMaxPrice());
            queryBuilder.must(rangequerybuilder);
        }
        if (request.getMaxPrice() > 0)
        {
            // 价格区间只有最大值
            RangeQueryBuilder rangequerybuilder = QueryBuilders.rangeQuery(ESConstants.PRICE)
                    .lte(request.getMaxPrice());
            queryBuilder.must(rangequerybuilder);
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

        // 是否有货过滤
        if (INVALID_FLAG != request.getHasStock())
        {
            // 无货
            queryBuilder.must(QueryBuilders.matchQuery(HAS_STOCK, request.getHasStock()));
        }

        // 商品分类过滤查询
        if (StringUtils.isNotBlank(request.getOprtCatNo()))
        {
            queryBuilder.must(QueryBuilders.matchQuery(ES_OPRTCATNO_FEILD, request.getOprtCatNo()));
        }
    }
}
