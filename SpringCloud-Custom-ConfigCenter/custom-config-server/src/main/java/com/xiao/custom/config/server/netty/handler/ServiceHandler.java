package com.xiao.custom.config.server.netty.handler;

import com.xiao.custom.config.server.manager.ClientManagerService;
import com.xiao.custom.config.server.netty.dto.CommandEnum;
import com.xiao.custom.config.server.netty.dto.Message;
import com.xiao.custom.config.server.netty.manager.Connection;
import com.xiao.custom.config.server.netty.manager.ConnectionManager;
import com.xiao.custom.config.server.netty.util.RemotingUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * [简要描述]: 业务handler
 * [详细描述]:
 * ##@Sharable 注解用来说明ChannelHandler是否可以在多个channel直接共享使用,单例的ChannelHandler<br>
 * 业务处理，请使用线程池来处理<br>
 *
 * @author llxiao
 * @version 1.0, 2019/3/30 16:00
 * @since JDK 1.8
 */
@Slf4j
@ChannelHandler.Sharable
public class ServiceHandler extends SimpleChannelInboundHandler<Message>
{
    private ClientManagerService clientManagerService;

    private ConnectionManager connectionManager;

    public ServiceHandler(ClientManagerService clientManagerService, ConnectionManager connectionManager)
    {
        super();
        this.clientManagerService = clientManagerService;
        this.connectionManager = connectionManager;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        log.info(">>> 客户端-IP:{},PORT:{},连接到服务器...", RemotingUtil.parseRemoteIP(ctx.channel()), RemotingUtil
                .parseRemotePort(ctx.channel()));
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception
    {

        String nettyIp = RemotingUtil.parseRemoteIP(channelHandlerContext.channel());
        int nettyPort = RemotingUtil.parseRemotePort(channelHandlerContext.channel());
        if (CommandEnum.IDLE.getStatus() == message.getCommand())
        {
            if (log.isDebugEnabled())
            {
                log.debug(">>> 客户端-IP:{},PORT:{}发起了心跳请求.", nettyIp, nettyPort);
            }
            //收到心跳维护客户端http服务和netty服务关系
            channelHandlerContext.channel().writeAndFlush(message);
        }
        else if (CommandEnum.LOGIN.getStatus() == message.getCommand())
        {
            log.info(">>> 客户端-IP:{},PORT:{}发起了登录请求", message.getHostIp(), message.getServerPort());
            log.info(">>> 客户端-Netty IP:{},Netty PORT:{}发起了登录请求", nettyIp, nettyPort);
            //绑定应用信息和netty端口信息
            clientManagerService.updateNettyInfo(message.getHostIp(), message.getServerPort(), nettyPort, nettyIp);
            connectionManager.addConnection(new Connection(channelHandlerContext.channel(), message.getHostIp(), message
                    .getServerPort()));
        }
        else
        {
            log.info(">>> 业务处理......");
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        String ip = RemotingUtil.parseRemoteIP(ctx.channel());
        int port = RemotingUtil.parseRemotePort(ctx.channel());
        log.error(">>> 客户端-IP:{},PORT:{}关闭了连接,并进行下线操作!", ip, port);
        // 下线处理
        if (ctx.channel().isOpen())
        {
            ctx.close();
        }
        clientManagerService.updateStatus(ip, port, ClientManagerService.OFF_LINE);
        connectionManager.remove(ip, port);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception
    {
        if (log.isDebugEnabled())
        {
            log.debug(">>> 已超过5S，未收到客户端发送到的消息.....");
        }

        if (evt instanceof IdleStateEvent)
        {
            //读取心跳超时后，会将此channel连接断开
            try
            {
                String ip = RemotingUtil.parseRemoteIP(ctx.channel());
                int port = RemotingUtil.parseRemotePort(ctx.channel());
                log.warn(">>> 关闭这个不活跃的连接-IP:{},PORT:{}并进行离线操作", ip, port);
                //  心跳，关闭连接
                if (ctx.channel().isOpen())
                {
                    ctx.close();
                    clientManagerService.updateStatus(ip, port, ClientManagerService.OFF_LINE);
                    connectionManager.remove(ip, port);
                }
            }
            catch (Exception e)
            {
                log.warn(">>> 关闭链路异常....", e);
            }
        }
        else
        {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        String ip = RemotingUtil.parseRemoteIP(ctx.channel());
        int port = RemotingUtil.parseRemotePort(ctx.channel());
        //        log.warn(">>> 客户端已断开连接......:{}", ctx);
        log.warn(">>> 关闭这个不活跃的连接-IP:{},PORT:{}并进行离线操作", ip, port);
        // 下线处理
        if (ctx.channel().isOpen())
        {
            ctx.close();
        }
        clientManagerService.updateStatus(ip, port, ClientManagerService.OFF_LINE);
        connectionManager.remove(ip, port);
        super.channelInactive(ctx);
    }
}
