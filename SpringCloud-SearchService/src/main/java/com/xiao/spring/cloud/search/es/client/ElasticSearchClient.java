/*
 * Winner
 * 文件名  :ElasticSearchClient.java
 * 创建人  :llxiao
 * 创建时间:2018年1月11日
 */

package com.xiao.spring.cloud.search.es.client;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * [简要描述]:ElasticSearch client类<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年1月11日
 * @since Purcotton-Search B01
 */
@Component
public class ElasticSearchClient
{
    /**
     * 日志记录器
     */
    private static final Log LOG = LogFactory.getLog(ElasticSearchClient.class);

    /**
     * ip端口组成数组长度
     */
    private static final int IP_AND_PORT_ARRAY_LENGTH = 2;

    /**
     * 集群配置名称
     */
    private static final String ES_CLUSTER_NAME_CONF = "cluster.name";

    /**
     * 自动嗅探配置
     */
    private static final String ES_SNIFF_CONF = "client.transport.sniff";

    /**
     * es集群主机
     */
    @Value("${elastic.search.host}")
    private String esHosts;

    /**
     * 集群名称
     */
    @Value("${elastic.search.cluster.name}")
    private String clusterName;

    /**
     * es 客户端 Transport Client
     */
    private TransportClient transoportClient;

    /**
     * client初始化是否OK
     */
    private boolean isOk = false;

    /**
     * [简要描述]:服务停止，关闭客户端<br/>
     * [详细描述]:<br/>
     *
     * @exception Exception
     */
    @PreDestroy
    public void destroy()
    {
        LOG.warn("Close elastic search transoport Client beacuase purcotton search server stoped!");
        if (isOk)
        {
            transoportClient.close();
        }

    }

    /**
     * [简要描述]:初始化ES client<br/>
     * [详细描述]:<br/>
     *
     * @exception UnknownHostException
     */
    @SuppressWarnings("resource")
    @PostConstruct
    public void init() throws UnknownHostException
    {
        if (StringUtils.isNotBlank(esHosts) || esHosts.split(":").length == IP_AND_PORT_ARRAY_LENGTH)
        {
            if (LOG.isInfoEnabled())
            {
                LOG.info("ElasticSearch Host configuration:" + esHosts);
            }

            String[] ipHost = esHosts.split(":");
            // 设置ES实例的名称
            Settings esSettings = Settings.builder().put(ES_CLUSTER_NAME_CONF, clusterName)
                    // 自动嗅探整个集群的状态，把集群中其他ES节点的ip添加到本地的客户端列表中
                    .put(ES_SNIFF_CONF, true).build();
            TransportAddress transportAddress = new TransportAddress(new InetSocketAddress(InetAddress
                    .getByName(ipHost[0]), Integer.parseInt(ipHost[1])));
            transoportClient = new PreBuiltTransportClient(esSettings).addTransportAddress(transportAddress);
            isOk = true;
        }
        else
        {
            LOG.error("Wrong Host configuration for ElasticSearch!");
        }
    }

    /**
     * [简要描述]:get TransportClient<br/>
     * [详细描述]:<br/>
     *
     * @return
     */
    public TransportClient getTransportClient()
    {
        return this.transoportClient;
    }
}
