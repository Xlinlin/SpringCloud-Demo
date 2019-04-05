package com.xiao.custom.config.server.netty.manager;

import com.xiao.custom.config.server.netty.util.RemotingUtil;
import io.netty.channel.Channel;
import lombok.Data;

/**
 * [简要描述]: channel连接
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/4/4 08:44
 * @since JDK 1.8
 */
@Data
public class Connection
{
    public static final String SPLIT = ":";

    private String uniqueKey;
    private String nettyIp;
    private int nettyPort;
    private String serverIp;
    private int serverPort;
    private Channel channel;

    public Connection()
    {

    }

    public Connection(Channel channel)
    {
        this.nettyIp = RemotingUtil.parseRemoteIP(channel);
        this.nettyPort = RemotingUtil.parseRemotePort(channel);
        this.uniqueKey = nettyIp + SPLIT + nettyPort;
        this.channel = channel;
    }

    public Connection(Channel channel, String serverIp, int serverPort)
    {
        this.nettyIp = RemotingUtil.parseRemoteIP(channel);
        this.nettyPort = RemotingUtil.parseRemotePort(channel);
        this.uniqueKey = nettyIp + SPLIT + nettyPort;
        this.channel = channel;
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }
}
