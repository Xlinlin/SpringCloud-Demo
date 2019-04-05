package com.xiao.custom.config.server.netty.manager;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * [简要描述]: 连接数管理
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/4/4 08:40
 * @since JDK 1.8
 */
@Component("connectionManager")
@Slf4j
public class ConnectionManager
{
    /**
     * 总连接数
     */
    private ConcurrentHashMap<String, Connection> conns = new ConcurrentHashMap();

    /**
     * 活跃连接数
     */
    private AtomicLong activeConnect = new AtomicLong(0);

    /**
     * [简要描述]:添加一个Channel连接<br/>
     * [详细描述]:<br/>
     *
     * @param connection :
     * llxiao  2019/4/4 - 8:54
     **/
    public void addConnection(Connection connection)
    {
        if (null != connection)
        {
            log.info(">>> 新增了一个连接：{}", connection);
            if (log.isDebugEnabled())
            {
                log.debug(">>> 新增了一个连接：{}", connection);
            }
            conns.put(connection.getUniqueKey(), connection);
            activeConnect.incrementAndGet();
        }
    }

    /**
     * [简要描述]:Key 获取一个连接<br/>
     * [详细描述]:找不到返回一个null<br/>
     *
     * @param uniqueKey :
     * @return com.winner.config.center.server.netty.manager.Connection
     * llxiao  2019/4/4 - 8:57
     **/
    public Connection getConnection(String uniqueKey)
    {
        Connection connection = null;
        if (StringUtils.isNotBlank(uniqueKey))
        {
            connection = conns.get(uniqueKey);
        }
        return connection;
    }

    /**
     * [简要描述]:依据IP、PORT查找连接信息<br/>
     * [详细描述]:找不到返回一个null<br/>
     *
     * @param nettyIp :
     * @param nettyPot :
     * @return com.winner.config.center.server.netty.manager.Connection
     * llxiao  2019/4/4 - 9:02
     **/
    public Connection getConnection(String nettyIp, int nettyPot)
    {
        Connection connection = null;
        if (StringUtils.isNotBlank(nettyIp))
        {
            connection = this.getConnection(nettyIp + Connection.SPLIT + nettyPot);
        }
        return connection;
    }

    /**
     * [简要描述]:依据IP、PORT查找连接信息<br/>
     * [详细描述]:找不到返回一个null<br/>
     *
     * @param nettyIp :
     * @param nettyPot :
     * @return java.nio.channels.Channel
     * llxiao  2019/4/4 - 9:04
     **/
    public Channel getChannel(String nettyIp, int nettyPot)
    {
        Channel channel = null;
        if (StringUtils.isNotBlank(nettyIp))
        {
            Connection connection = this.getConnection(nettyIp, nettyPot);
            if (null != connection)
            {
                channel = connection.getChannel();
            }
        }
        return channel;
    }

    /**
     * [简要描述]:Key 获取一个连接<br/>
     * [详细描述]:找不到返回一个null<br/>
     *
     * @param uniqueKey :
     * @return io.netty.channel.Channel
     * llxiao  2019/4/4 - 9:08
     **/
    public Channel getChannel(String uniqueKey)
    {
        Channel channel = null;
        if (StringUtils.isNotBlank(uniqueKey))
        {
            Connection connection = this.getConnection(uniqueKey);
            if (null != connection)
            {
                channel = connection.getChannel();
            }
        }
        return channel;
    }

    /**
     * [简要描述]:删除一个连接<br/>
     * [详细描述]:<br/>
     *
     * @param uniqueKey :
     * @return void
     * llxiao  2019/4/4 - 9:09
     **/
    public void remove(String uniqueKey)
    {
        if (StringUtils.isNotBlank(uniqueKey))
        {
            Connection connection = conns.remove(uniqueKey);
            if (null != connection)
            {
                log.info(">>> 删除了一个连接：{}", connection);
                if (log.isDebugEnabled())
                {
                    log.debug(">>> 删除了一个连接：{}", connection);
                }
                activeConnect.decrementAndGet();
            }
        }
    }

    /**
     * [简要描述]:删除一个连接<br/>
     * [详细描述]:<br/>
     *
     * @param nettyIp :
     * @param nettyPort :
     * @return void
     * llxiao  2019/4/4 - 9:09
     **/
    public void remove(String nettyIp, int nettyPort)
    {
        if (StringUtils.isNotBlank(nettyIp))
        {
            String uniqueKey = nettyIp + Connection.SPLIT + nettyPort;
            this.remove(uniqueKey);
        }
    }

    /**
     * [简要描述]:获取当前总连接数<br/>
     * [详细描述]:<br/>
     *
     * @return long
     * llxiao  2019/4/4 - 9:14
     **/
    public long getCurrentConnectNum()
    {
        return activeConnect.get();
    }
}
