package com.xiao.springcloud.test;

import com.xiao.spring.cloud.search.dto.ElasticSearchDoc;
import com.xiao.spring.cloud.search.es.client.ElasticSearchClient;
import com.xiao.spring.cloud.search.service.SearchManangerService;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/10/9 11:07
 * @since JDK 1.8
 */
public class SearchManagerTest extends SearchApplicationTest
{
    @Autowired
    private SearchManangerService searchManangerService;

    @Autowired
    private ElasticSearchClient esClient;

    @Test
    public void testGetByCommoNo() throws Exception
    {
        this.isPrint = true;
        String url = "/search/manager/getById";
        Map<String, String> params = new HashMap<>(1);
        params.put("id", "002000000662");
        params.put("index", "purcotton");
        // 字符串包含Contains
        //        this.testBasePostApi(url, params, null)
        //                .andExpect(MockMvcResultMatchers.content().string(new Contains("002000000662")));
        // json ID处理
        this.testBasePostApi(url, params, null).andExpect(MockMvcResultMatchers.jsonPath("$.id").value("002000000662"));
    }

    @Test
    public void testAddDoc()
    {
        ElasticSearchDoc doc = new ElasticSearchDoc();
        doc.setBrandName("苹果");
        doc.setBrandNo("10000");
        doc.setId("50101001");
        doc.setCategoryName("品牌手机");
        doc.setCategoryNo("001001001");
        doc.setComments(5);
        doc.setCommoNo("50101001");
        doc.setDefProdNo("50101001001");
        doc.setDiscountRate("9");
        doc.setTitle("IPhone XS Max 全网通256G");
        doc.setOldPrice(9888.00);
        doc.setSalePrice(8888.00);
        doc.setHasStock(1);
        doc.setSubTitle("金色，新春贺礼大礼包，送家人送朋友送亲戚!!");
        doc.setSalesVolume(500);
        doc.setSalesLabels("新品、特卖、新春贺礼");
        doc.setIndex("10000");
        doc.setKeyWords("IPhone XS Max 全网通256G，金色，新春贺礼大礼包，送家人送朋友送亲戚!!");
        doc.setProdPicUrl("");
        searchManangerService.addData(doc);

        doc = new ElasticSearchDoc();
        doc.setBrandName("苹果");
        doc.setBrandNo("10000");
        doc.setCategoryName("配件");
        doc.setCategoryNo("001001002");
        doc.setComments(5);
        doc.setCommoNo("50101002");
        doc.setId("50101002");
        doc.setDefProdNo("50101002001");
        doc.setDiscountRate("5");
        doc.setTitle("IPhone无线充电器");
        doc.setOldPrice(588);
        doc.setSalePrice(288);
        doc.setHasStock(1);
        doc.setSubTitle("金色，新春贺礼大礼包，IPhone XS Max贴身伴侣");
        doc.setSalesVolume(20);
        doc.setSalesLabels("新品、特卖、新春贺礼");
        doc.setIndex("10000");
        doc.setKeyWords("IPhone无线充电器，金色，新春贺礼大礼包，IPhone XS Max贴身伴侣");
        doc.setProdPicUrl("");
        searchManangerService.addData(doc);

        doc = new ElasticSearchDoc();
        doc.setBrandName("苹果");
        doc.setBrandNo("10000");
        doc.setId("50101003");
        doc.setCategoryName("品牌手机");
        doc.setCategoryNo("001001001");
        doc.setComments(5);
        doc.setCommoNo("50101003");
        doc.setDefProdNo("50101003001");
        doc.setDiscountRate("9");
        doc.setTitle("IPhone 8 Plus 128G");
        doc.setOldPrice(6888.00);
        doc.setSalePrice(5888.00);
        doc.setHasStock(1);
        doc.setSubTitle("白色，送家人送朋友送亲戚!!");
        doc.setSalesVolume(1000);
        doc.setSalesLabels("降价、特卖");
        doc.setIndex("10000");
        doc.setKeyWords("IPhone 8 Plus 128G，白色，降价，送家人送朋友送亲戚!!");
        doc.setProdPicUrl("");
        searchManangerService.addData(doc);



        doc = new ElasticSearchDoc();
        doc.setBrandName("苹果");
        doc.setBrandNo("10000");
        doc.setCategoryName("耳机");
        doc.setCategoryNo("001001003");
        doc.setComments(5);
        doc.setCommoNo("50101004");
        doc.setId("50101004");
        doc.setDefProdNo("50101004001");
        doc.setDiscountRate("5");
        doc.setTitle("Airpods IPhone绝配耳机");
        doc.setOldPrice(1388);
        doc.setSalePrice(1288);
        doc.setHasStock(1);
        doc.setSubTitle("新春贺礼大礼包，IPhone贴身伴侣");
        doc.setSalesVolume(2);
        doc.setSalesLabels("新品、特卖、新春贺礼");
        doc.setIndex("10000");
        doc.setKeyWords("Airpods IPhone绝配耳机，新春贺礼大礼包，IPhone贴身伴侣");
        doc.setProdPicUrl("");
        searchManangerService.addData(doc);
    }

    @Test
    public void testDel()
    {
        searchManangerService.deleteData("50101001", "10000");
        searchManangerService.deleteData("50101002", "10000");
        searchManangerService.deleteData("50101003", "10000");
        searchManangerService.deleteData("50101004", "10000");
    }

    /**
     * [简要描述]:在Index为10000下查找标题包含“IPhone”,优先取“品牌手机”这个分类，销量越高越前，结果随机给用户展示<br/>
     * [详细描述]:<br/>
     * <p>
     * llxiao  2019/1/31 - 15:44
     **/
    @Test
    public void testFilterQuery()
    {
        String searchContent = "IPhone";
        TransportClient client = esClient.getTransportClient();
        String index = "10000";
        SearchRequestBuilder searchBuilder = client.prepareSearch(index);
        //分页
        searchBuilder.setFrom(0).setSize(10);
        //explain为true表示根据数据相关度排序，和关键字匹配最高的排在前面
        searchBuilder.setExplain(true);

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        // 搜索 title字段包含IPhone的数据
        queryBuilder.must(QueryBuilders.matchQuery("title", searchContent));

        FunctionScoreQueryBuilder.FilterFunctionBuilder[] filterFunctionBuilders = new FunctionScoreQueryBuilder.FilterFunctionBuilder[3];

        //过滤条件1：分类为：品牌手机最重要 -- 权重查询Weight
        ScoreFunctionBuilder<WeightBuilder> scoreFunctionBuilder = new WeightBuilder();
        scoreFunctionBuilder.setWeight(2);
        QueryBuilder termQuery = QueryBuilders.termQuery("categoryName", "品牌手机");
        FunctionScoreQueryBuilder.FilterFunctionBuilder category = new FunctionScoreQueryBuilder.FilterFunctionBuilder(termQuery, scoreFunctionBuilder);
        filterFunctionBuilders[0] = category;

        // 过滤条件2：销量越高越排前 --计分查询 FieldValueFactor
        ScoreFunctionBuilder<FieldValueFactorFunctionBuilder> fieldValueScoreFunction = new FieldValueFactorFunctionBuilder("salesVolume");
        ((FieldValueFactorFunctionBuilder) fieldValueScoreFunction).factor(1.2f);
        FunctionScoreQueryBuilder.FilterFunctionBuilder sales = new FunctionScoreQueryBuilder.FilterFunctionBuilder(fieldValueScoreFunction);
        filterFunctionBuilders[1] = sales;

        // 给定每个用户随机展示：  --random_score
        ScoreFunctionBuilder<RandomScoreFunctionBuilder> randomScoreFilter = new RandomScoreFunctionBuilder();
        ((RandomScoreFunctionBuilder) randomScoreFilter).seed(2);
        FunctionScoreQueryBuilder.FilterFunctionBuilder random = new FunctionScoreQueryBuilder.FilterFunctionBuilder(randomScoreFilter);
        filterFunctionBuilders[2] = random;

        // 多条件查询 FunctionScore
        FunctionScoreQueryBuilder query = QueryBuilders.functionScoreQuery(queryBuilder, filterFunctionBuilders)
                .scoreMode(FunctionScoreQuery.ScoreMode.SUM).boostMode(CombineFunction.SUM);
        searchBuilder.setQuery(query);

        SearchResponse response = searchBuilder.execute().actionGet();
        SearchHits hits = response.getHits();
        String searchSource;
        for (SearchHit hit : hits)
        {
            searchSource = hit.getSourceAsString();
            System.out.println(searchSource);
        }
        //        long took = response.getTook().getMillis();
        long total = hits.getTotalHits();
        System.out.println(total);

    }

}
