package com.xiao.custom.elasticsearch.starter.example;

import com.xiao.custom.elasticsearch.start.autoconfig.properties.ElasticsearchProperties;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.close.CloseIndexRequest;
import org.elasticsearch.action.admin.indices.close.CloseIndexResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexResponse;
import org.elasticsearch.action.bulk.*;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedRequest;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.get.GetResult;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/8/28 13:56
 * @since JDK 1.8
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticsearchApplicationTest
{
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private ElasticsearchProperties elasticsearchProperties;

    /**
     * 搜索
     * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/6.2/java-rest-high-search.html
     */
    @Test
    public void testQuery()
    {
        SearchRequest request = new SearchRequest("10000");
        //        request.searchType("10000");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termQuery("commodityNo", "3439538790"));

        searchSourceBuilder.from(0);
        searchSourceBuilder.size(10);
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        // 高亮设置
        HighlightBuilder highlightBuilder = new HighlightBuilder().field("*").requireFieldMatch(false);
        if (StringUtils.isNoneEmpty(elasticsearchProperties.getHighlightPre()))
        {
            highlightBuilder.preTags(elasticsearchProperties.getHighlightPre());
            highlightBuilder.postTags(elasticsearchProperties.getHighlightPost());
            highlightBuilder.field("subTitle");
            highlightBuilder.field("title");
        }
        searchSourceBuilder.highlighter(highlightBuilder);

        request.source(searchSourceBuilder);

        try
        {
            final SearchResponse response = restHighLevelClient.search(request);
            response.getHits().forEach(document -> System.out.println(document.getSourceAsString()));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 批处理：
     * 增删改查
     */
    public void testBulkRequest()
    {
        BulkRequest request = new BulkRequest();
        request.add(new DeleteRequest("posts", "doc", "3"));
        request.add(new UpdateRequest("posts", "doc", "2").doc(XContentType.JSON, "other", "test"));
        request.add(new IndexRequest("posts", "doc", "4").source(XContentType.JSON, "field", "baz"));

        try
        {
            // restHighLevelClient.bulkAsync(request, ActionListener);
            BulkResponse bulkResponse = restHighLevelClient.bulk(request);
            if (bulkResponse.hasFailures())
            {
                for (BulkItemResponse bulkItemResponse : bulkResponse)
                {
                    if (bulkItemResponse.isFailed())
                    {
                        BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
                        System.out.println("处理失败:" + failure.getId() + '-' + failure.getMessage());
                    }
                }
            }

            for (BulkItemResponse bulkItemResponse : bulkResponse)
            {
                DocWriteResponse itemResponse = bulkItemResponse.getResponse();

                if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.INDEX
                        || bulkItemResponse.getOpType() == DocWriteRequest.OpType.CREATE)
                {
                    IndexResponse indexResponse = (IndexResponse) itemResponse;
                    // 创建

                }
                else if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.UPDATE)
                {
                    UpdateResponse updateResponse = (UpdateResponse) itemResponse;
                    // 更新

                }
                else if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.DELETE)
                {
                    DeleteResponse deleteResponse = (DeleteResponse) itemResponse;
                    // 删除
                }
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * BulkProcessor通过提供一个实用程序类简化了批量API的使用，该实用程序类允许在将索引/更新/删除操作添加到处理器时透明地执行这些操作。
     */
    @Test
    public void testBulkProcessor()
    {
        BulkProcessor.Listener listener = new BulkProcessor.Listener()
        {
            @Override
            public void beforeBulk(long executionId, BulkRequest request)
            {
                int numberOfActions = request.numberOfActions();
                System.out.println("当前BulkProcessor中执行的操作数:" + numberOfActions);
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, BulkResponse response)
            {
                if (response.hasFailures())
                {
                    System.out.println("当前BulkProcessor执行出现异常：" + response.buildFailureMessage());
                }
                else
                {
                    System.out.println("执行成功!");
                }
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, Throwable failure)
            {
                System.out.println("当前请求发生的错误消息：" + failure.getMessage());
                failure.printStackTrace();
            }
        };

        BulkProcessor.Builder builder = BulkProcessor.builder(restHighLevelClient::bulkAsync, listener);
        //根据当前添加的操作数设置刷新新批量请求的时间 defaults to 1000
        builder.setBulkActions(500);
        // 根据当前添加的操作的大小设置刷新新批量请求的时间 defaults to 5Mb
        builder.setBulkSize(new ByteSizeValue(1L, ByteSizeUnit.MB));
        // 设置允许执行的并发请求数 默认0仅允许一个
        builder.setConcurrentRequests(0);
        // 设置刷新间隔，如果间隔通过，则刷新任何挂起的BulkRequest
        builder.setFlushInterval(TimeValue.timeValueSeconds(10L));
        // 回退策略 等待1秒，最多重试3
        builder.setBackoffPolicy(BackoffPolicy.constantBackoff(TimeValue.timeValueSeconds(1L), 3));

        BulkProcessor bulkProcessor = builder.build();
        IndexRequest one = new IndexRequest("posts", "doc", "1").
                source(XContentType.JSON, "title", "In which order are my Elasticsearch queries executed?");
        IndexRequest two = new IndexRequest("posts", "doc", "2")
                .source(XContentType.JSON, "title", "Current status and upcoming changes in Elasticsearch");
        IndexRequest three = new IndexRequest("posts", "doc", "3")
                .source(XContentType.JSON, "title", "The Future of Federated Search in Elasticsearch");
        bulkProcessor.add(one);
        bulkProcessor.add(two);
        bulkProcessor.add(three);

        try
        {
            // 执行并等待，直到超时
            boolean terminated = bulkProcessor.awaitClose(30L, TimeUnit.SECONDS);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        // 关闭
        bulkProcessor.close();
    }

    /**
     * 插入数据 index api
     */
    @Test
    public void testInsert() throws IOException
    {
        String index = "101010";
        String type = index;
        String id = UUID.randomUUID().toString();
        IndexRequest indexRequest = new IndexRequest(index, type, id);

        // 可以设置版本号，但可能出现版本冲突异常  ElasticsearchException  e.status() == RestStatus.CONFLICT
        indexRequest.version(1);
        // or
        //        IndexRequest indexRequest = new IndexRequest();
        //        indexRequest.index(index);
        //        indexRequest.type(type);
        //        indexRequest.id(id);

        // As json String
        String jsonString = "{" + "\"user\":\"kimchy\"," + "\"postDate\":\"2013-01-30\","
                + "\"message\":\"trying out Elasticsearch\"" + "}";
        indexRequest.source(jsonString, XContentType.JSON);

        // As map
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("user", "kimchy");
        jsonMap.put("postDate", new Date());
        jsonMap.put("message", "trying out Elasticsearch");
        indexRequest.source(jsonMap);

        // As XContentBuilder

        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.field("user", "kimchy");
            builder.field("postDate", new Date());
            builder.field("message", "trying out Elasticsearch");
        }
        builder.endObject();
        indexRequest.source(builder);

        // As source
        indexRequest.source("user", "kimchy", "postDate", new Date(), "message", "trying out Elasticsearch");

        // index or indexAsync
        IndexResponse indexResponse = restHighLevelClient.index(indexRequest);

        index = indexResponse.getIndex();
        type = indexResponse.getType();
        id = indexResponse.getId();
        long version = indexResponse.getVersion();
        if (indexResponse.getResult() == DocWriteResponse.Result.CREATED)
        {
            System.out.println("ES数据已经成功创建");
        }
        else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED)
        {
            System.out.println("ES数据已经成功覆盖");
        }
        ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
        if (shardInfo.getTotal() != shardInfo.getSuccessful())
        {
            System.out.println("数据创建成功，但成功的shard数量小于总shard数量");
        }
        if (shardInfo.getFailed() > 0)
        {
            for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures())
            {
                System.out.println("失败原：" + failure.reason());
            }
        }

    }

    /**
     * 文档ID 查找
     */
    @Test
    public void testGetIndex()
    {
        GetRequest getRequest = new GetRequest("101010", "101010", "101010");
        // 设置版本
        //        getRequest.version(2);

        //禁用获取 _source字段
        getRequest.fetchSourceContext(new FetchSourceContext(false));

        try
        {
            // get or getAsync(request,ActionListener)
            GetResponse getResponse = restHighLevelClient.get(getRequest);
            String index = getResponse.getIndex();
            String type = getResponse.getType();
            String id = getResponse.getId();
            if (getResponse.isExists())
            {
                long version = getResponse.getVersion();
                String sourceAsString = getResponse.getSourceAsString();
                Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
                byte[] sourceAsBytes = getResponse.getSourceAsBytes();
            }
            else
            {
                System.out.println("不存在的数据!");
            }
        }
        catch (ElasticsearchException e)
        {
            if (e.status() == RestStatus.NOT_FOUND)
            {
                System.out.println("索引不存在");
            }
            else if (e.status() == RestStatus.CONFLICT)
            {
                System.out.println("版本冲突!");
            }
            else
            {
                System.out.println("其他为未知异常：" + e.status().name());
            }
        }
        catch (IOException e)
        {
            System.out.println("IO 异常");
        }

    }

    /**
     * 文档ID 更新
     * 1. 脚本更新
     * 2. 文档更新：部分字段更新和不存在直接插入更新
     */
    @Test
    public void testUpdate() throws IOException
    {
        UpdateRequest request = new UpdateRequest("101010", "101010", "101010");

        // ############## 更新文档数据部分字段使用 .doc ，如果不确定存在则直接插入使用 .upsert方法
        // As  JSON String
        String jsonString = "{" + "\"updated\":\"2017-01-01\"," + "\"reason\":\"daily update\"" + "}";
        request.doc(jsonString, XContentType.JSON);
        //        request.upsert(jsonString,XContentType.JSON);

        // As Map
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("updated", new Date());
        jsonMap.put("reason", "daily update");
        request.doc(jsonMap);
        //        request.upsert(jsonMap);

        // AS XContentBuilder
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.field("updated", new Date());
            builder.field("reason", "daily update");
        }
        builder.endObject();
        request.doc(builder);
        request.upsert(builder);

        // 设置版本
        //        request.version(2);

        //指示如果部分文档尚不存在，则必须将其用作upsert文档。
        //        request.docAsUpsert(true);

        try
        {
            //同步 update 异步 updateAsync(request,ActionListener)
            UpdateResponse updateResponse = restHighLevelClient.update(request);
            String index = updateResponse.getIndex();
            String type = updateResponse.getType();
            String id = updateResponse.getId();
            long version = updateResponse.getVersion();
            if (updateResponse.getResult() == DocWriteResponse.Result.CREATED)
            {
                System.out.println("文档不存在，创建成功!");
            }
            else if (updateResponse.getResult() == DocWriteResponse.Result.UPDATED)
            {
                System.out.println("文档更新成功!");
            }
            else if (updateResponse.getResult() == DocWriteResponse.Result.DELETED)
            {
                System.out.println("文档删除成功!");
            }
            else if (updateResponse.getResult() == DocWriteResponse.Result.NOOP)
            {
                System.out.println("没有对文档做任何更新操作!");
            }

            // 获取更新后的数据
            GetResult result = updateResponse.getGetResult();
            if (result.isExists())
            {
                String sourceAsString = result.sourceAsString();
                Map<String, Object> sourceAsMap = result.sourceAsMap();
                byte[] sourceAsBytes = result.source();
            }
            else
            {
                System.out.println("文档不存在!");
            }

            // 分片更新失败
            ReplicationResponse.ShardInfo shardInfo = updateResponse.getShardInfo();
            if (shardInfo.getFailed() > 0)
            {
                for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures())
                {
                    System.out.println("分片ID:" + failure.fullShardId() + "更新失败：" + failure.reason());
                }
            }
        }
        catch (ElasticsearchException e)
        {
            // 可能有索引没找到，版本异常，参考RestStatus
            System.out.println("更新异常：" + e.status().name());
        }
    }

    /**
     * 文档ID 删除
     */
    @Test
    public void testDel()
    {
        DeleteRequest request = new DeleteRequest("101010", "101010", "101010");
        try
        {
            DeleteResponse deleteResponse = restHighLevelClient.delete(request);
            if (deleteResponse.getResult() == DocWriteResponse.Result.NOT_FOUND)
            {
                System.out.println("删除的文档不存在");
            }

            ReplicationResponse.ShardInfo shardInfo = deleteResponse.getShardInfo();
            if (shardInfo.getTotal() != shardInfo.getSuccessful())
            {
                System.out.println("删除成功了，但是删除的数量与分片的数量不符合!");
            }
            if (shardInfo.getFailed() > 0)
            {
                for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures())
                {
                    System.out.println("分片ID:" + failure.fullShardId() + "更新失败：" + failure.reason());
                }
            }
        }
        catch (ElasticsearchException e)
        {
            System.out.println("删除异常：" + e.status().name());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 创建索引
     *
     * @exception IOException
     */
    @Test
    public void testCreateIndex() throws IOException
    {
        CreateIndexRequest request = new CreateIndexRequest("101010");
        // setting设置
        request.settings(Settings.builder().put("index.number_of_shards", 3).put("index.number_of_replicas", 2));
        // mapping
        //        request.mapping();
        request.alias(new Alias("101010_alias"));

        // 其他可选参数
        setOptionParams(request);

        // 同步创建
        final CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request);
        // 所有节点是否已确认请求
        if (createIndexResponse.isAcknowledged())
        {
            System.out.println("创建成功!");
        }
        // 是否在超时前为索引中的每个碎片启动了所需数量的分片副本
        if (createIndexResponse.isShardsAcknowledged())
        {
            System.out.println("分片副本都已创建成功");
        }

        // 异步创建
        //        restHighLevelClient.indices().createAsync(request, new ActionListener<CreateIndexResponse>()
        //        {
        //            @Override
        //            public void onResponse(CreateIndexResponse clearIndicesCacheResponse)
        //            {
        //                System.out.println("所有节点是否已确认请求: " + createIndexResponse.isAcknowledged());
        //                System.out.println("分片副本都已创建成功:" + createIndexResponse.isShardsAcknowledged());
        //            }
        //
        //            @Override
        //            public void onFailure(Exception e)
        //            {
        //                System.out.println("请求出现异常，异常信息：" + e.getMessage());
        //                e.printStackTrace();
        //            }
        //        });
    }

    /**
     * 所以是否存在
     */
    @Test
    public void testIndexExist()
    {
        GetIndexRequest request = new GetIndexRequest();
        request.indices("10001");
        try
        {
            System.out.println(restHighLevelClient.indices().exists(request));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 删除索引
     */
    @Test
    public void testDelIndex()
    {
        DeleteIndexRequest request = new DeleteIndexRequest("101010");
        // 其他可选参数
        setOptionParams(request);

        // 同步
        try
        {
            DeleteIndexResponse response = restHighLevelClient.indices().delete(request);
            System.out.println("所有节点已确认：" + response.isAcknowledged());
        }
        catch (ElasticsearchException exception)
        {
            if (exception.status() == RestStatus.NOT_FOUND)
            {
                System.out.println("索引未找到!");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        // 异步
        //        restHighLevelClient.indices().deleteAsync(request, new ActionListener<DeleteIndexResponse>()
        //        {
        //            @Override
        //            public void onResponse(DeleteIndexResponse deleteIndexResponse)
        //            {
        //                System.out.println("所有节点已确认：" + deleteIndexResponse.isAcknowledged());
        //            }
        //
        //            @Override
        //            public void onFailure(Exception e)
        //            {
        //                if (e instanceof ElasticsearchException)
        //                {
        //                    ElasticsearchException exception = (ElasticsearchException) e;
        //                    if (exception.status() == RestStatus.NOT_FOUND)
        //                    {
        //                        System.out.println("索引未找到!");
        //                    }
        //                }
        //                System.out.println("删除出现位置异常!");
        //            }
        //        });
    }

    /**
     * 打开和关闭索引
     */
    @Test
    public void testOpenAndCloseIndex() throws IOException
    {
        OpenIndexRequest openIndexRequest = new OpenIndexRequest("index");
        setOptionParams(openIndexRequest);
        // 同步 open or 异步 openAsync
        OpenIndexResponse openIndexResponse = restHighLevelClient.indices().open(openIndexRequest);
        System.out.println("所有节点已确认：" + openIndexResponse.isAcknowledged());
        System.out.println("所有副本分片已确认：" + openIndexResponse.isShardsAcknowledged());

        CloseIndexRequest closeIndexRequest = new CloseIndexRequest("index");
        setOptionParams(closeIndexRequest);
        // 同步 close or 异步  closeAsync
        CloseIndexResponse closeIndexResponse = restHighLevelClient.indices().close(closeIndexRequest);
        System.out.println("所有节点已确认：" + closeIndexResponse.isAcknowledged());

    }

    private void setOptionParams(AcknowledgedRequest request)
    {
        // 其他可选参数
        request.timeout(TimeValue.timeValueMinutes(2));
        //        request.timeout("2m");

        // master node
        //        request.masterNodeTimeout(TimeValue.timeValueMinutes(1));
        //        request.masterNodeTimeout("1m");

        // 创建索引API返回响应之前等待的活动分片副本数
        //        request.waitForActiveShards(2);
        //        request.waitForActiveShards(ActiveShardCount.DEFAULT);
    }

}
