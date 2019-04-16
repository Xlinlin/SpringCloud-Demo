package com.xiao.custom.config.client.netty.handler;

import com.xiao.custom.config.client.netty.dto.CommandEnum;
import com.xiao.custom.config.client.netty.dto.Message;
import com.xiao.custom.config.client.netty.util.RemotingUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.refresh.ContextRefresher;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * [简要描述]: 服务端业务处理handler
 * [详细描述]:
 * 业务处理请使用线程池
 *
 * @author llxiao
 * @version 1.0, 2019/3/30 17:16
 * @since JDK 1.8
 */
@ChannelHandler.Sharable
@Slf4j
public class ServiceHandler extends SimpleChannelInboundHandler<Message>
{
    private static final Message HEARTBEAT_SEQUENCE = new Message();

    private AtomicBoolean stated;

    private ContextRefresher refresher;

    public ServiceHandler(AtomicBoolean stated, ContextRefresher refresher)
    {
        super();
        this.stated = stated;
        HEARTBEAT_SEQUENCE.setCommand(CommandEnum.IDLE.getStatus());
        this.refresher = refresher;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception
    {
        //心跳消息
        if (CommandEnum.IDLE.getStatus() == message.getCommand())
        {
            if (log.isDebugEnabled())
            {
                log.debug(">>> 接收到服务端返回心跳消息：{}", message);
            }
        }
        else if (CommandEnum.REFRESH.getStatus() == message.getCommand())
        {
            log.info(">>> 服务端推送了刷新信息", message.getCommand());
            refresher.refresh();
        }
        else
        {
            log.info(">>> 业务处理");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        log.warn(">>> 服务端异常，等待定时重连......");
        stated.set(false);
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception
    {

        if (evt instanceof IdleStateEvent)
        {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE)
            {
                //如果没有触发写事件则向服务器发送一次心跳包
                if (log.isDebugEnabled())
                {
                    log.debug(">>> 客户端-IP:{},PORT:{}不活跃了，发起心跳请求.....", RemotingUtil
                            .parseLocalIP(ctx.channel()), RemotingUtil.parseLocalPort(ctx.channel()));
                }
                // 发起心跳请求
                ctx.writeAndFlush(HEARTBEAT_SEQUENCE);
            }
            else if (state == IdleState.READER_IDLE)
            {
                //如果没有收到服务端的写 则表示服务器超时 判断是否断开连接
                log.warn("未收到服务器消息，服务器可能出现异常。需要进行重连操作..");
                stated.set(false);
                if (ctx.channel().isOpen())
                {
                    ctx.close();
                }
            }
            else
            {
                super.userEventTriggered(ctx, evt);
            }
        }
        else
        {
            super.userEventTriggered(ctx, evt);
        }
    }

    //建立连接时回调
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        log.info(">>> 建立连接成功，服务端IP:{},PORT:{}", RemotingUtil.parseRemoteIP(ctx.channel()), RemotingUtil
                .parseRemotePort(ctx.channel()));
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        log.warn(">>> 服务端不活跃了。IP:{},PORT:{}", RemotingUtil.parseRemoteIP(ctx.channel()), RemotingUtil
                .parseRemotePort(ctx.channel()));
        super.channelInactive(ctx);
    }
}
