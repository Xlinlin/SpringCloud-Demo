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

    public ServiceHandler(AtomicBoolean stated)
    {
        super();
        this.stated = stated;
        HEARTBEAT_SEQUENCE.setCommand(CommandEnum.IDLE.getStatus());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception
    {
        //心跳消息
        if (CommandEnum.IDLE.getStatus() == message.getCommand())
        {
            log.info(">>> 接收到服务端返回心跳消息：{}", message);
        }
        else
        {
            log.info("业务逻辑处理..:{}", message.getCommand());
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
                if (log.isDebugEnabled())
                {
                    log.info(">>> 客户端-IP:{},PORT:{}不活跃了，发起心跳请求.....", RemotingUtil
                            .parseLocalIP(ctx.channel()), RemotingUtil.parseLocalPort(ctx.channel()));
                }
                // 发起心跳请求
                ctx.writeAndFlush(HEARTBEAT_SEQUENCE);
            }
        }
        else
        {
            super.userEventTriggered(ctx, evt);
        }
    }
}
