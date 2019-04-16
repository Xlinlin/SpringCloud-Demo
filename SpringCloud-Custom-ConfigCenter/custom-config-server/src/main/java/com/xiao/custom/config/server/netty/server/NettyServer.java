package com.xiao.custom.config.server.netty.server;

import com.xiao.custom.config.server.manager.ClientManagerService;
import com.xiao.custom.config.server.netty.factory.CoderFactory;
import com.xiao.custom.config.server.netty.factory.NamedThreadFactory;
import com.xiao.custom.config.server.netty.handler.ServiceHandler;
import com.xiao.custom.config.server.netty.manager.ConnectionManager;
import com.xiao.custom.config.server.netty.util.NettyConfig;
import com.xiao.custom.config.server.netty.util.NettyEventLoopUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * [简要描述]: Netty服务
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/3/30 14:23
 * @since JDK 1.8
 */
@Component
@Slf4j
public class NettyServer implements ApplicationListener<ContextRefreshedEvent>, DisposableBean
{
    /**
     * 启动状态
     */
    private AtomicBoolean started = new AtomicBoolean(false);

    @Value("${netty.server.port}")
    private int port;

    @Autowired
    private ClientManagerService clientManagerService;

    @Autowired
    private ConnectionManager connectionManager;

    private ScheduledExecutorService scheduledExecutorService;

    private ExecutorService simpleExecutor;

    /**
     * server bootstrap
     */
    private ServerBootstrap serverBootstrap;

    /**
     * channelFuture
     */
    private ChannelFuture channelFuture;

    /**
     * boss event loop group, boss group should not be daemon, need shutdown manually
     */
    private final EventLoopGroup bossGroup = NettyEventLoopUtil
            .newEventLoopGroup(1, new NamedThreadFactory("Winner-netty-server-boss", false));

    /**
     * worker event loop group. Reuse I/O worker threads between rpc servers.
     */
    private static final EventLoopGroup workerGroup = NettyEventLoopUtil.newEventLoopGroup(
            Runtime.getRuntime().availableProcessors() * 2, new NamedThreadFactory("Winner-netty-server-worker", true));

    static
    {
        if (workerGroup instanceof NioEventLoopGroup)
        {
            ((NioEventLoopGroup) workerGroup).setIoRatio(NettyConfig.NETTY_IO_RATIO_DEFAULT);
        }
        else if (workerGroup instanceof EpollEventLoopGroup)
        {
            ((EpollEventLoopGroup) workerGroup).setIoRatio(NettyConfig.NETTY_IO_RATIO_DEFAULT);
        }
    }

    @Override
    public void destroy() throws Exception
    {
        stop();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent)
    {
        initNetty();
        setHandler();
        start();
        // 启动后5S开始检查Netty server的状态，每个2两分钟执行一次
        scheduledExecutorService.scheduleAtFixedRate(() -> check(), 1, 2, TimeUnit.MINUTES);
    }

    private void check()
    {
        if (!started.get())
        {
            log.info(">>> Re start netty server!");
            start();
        }
    }

    private void setHandler()
    {

        serverBootstrap.childHandler(new ChannelInitializer()
        {
            @Override
            protected void initChannel(Channel channel) throws Exception
            {
                ChannelPipeline pipeline = channel.pipeline();

                // 设置心跳，readerIdleTime：读空闲5S发起心跳处理，writerIdleTime：写空闲时间，allIdleTime：所有空闲时间,5S内没有 read动作，触发userEventTriggered动作
                pipeline.addLast(new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));

                //编解码
                pipeline.addLast(CoderFactory.newDecoder());
                pipeline.addLast(CoderFactory.newEncoder());

                // 业务handler
                pipeline.addLast(new ServiceHandler(clientManagerService, connectionManager));
            }
        });
    }

    private void start()
    {
        log.info(">>>> Netty Server start......");
        try
        {
            this.channelFuture = this.serverBootstrap.bind(new InetSocketAddress(port)).sync();
            started.set(this.channelFuture.isSuccess());
            log.info(">>>> Netty Server start successfully,Open port: {}", port);
        }
        catch (InterruptedException e)
        {
            log.error(">>>> Netty Server start failed!");
            log.error("Error message:", e);
        }
    }

    private void initNetty()
    {
        scheduledExecutorService = new ScheduledThreadPoolExecutor(1, NamedThreadFactory
                .create("Scheduled check netty server-", true));
        simpleExecutor = Executors.newSingleThreadExecutor(new NamedThreadFactory("Netty server-", true));

        log.info(">>>> Netty Server init......");
        this.serverBootstrap = new ServerBootstrap();
        this.serverBootstrap.group(bossGroup, workerGroup).channel(NettyEventLoopUtil.getServerSocketChannelClass())
                .option(ChannelOption.SO_BACKLOG, NettyConfig.TCP_SO_BACKLOG_DEFAULT)
                .option(ChannelOption.SO_REUSEADDR, NettyConfig.TCP_SO_REUSEADDR_DEFAULT)
                .childOption(ChannelOption.TCP_NODELAY, NettyConfig.TCP_NODELAY_DEFAULT)
                .childOption(ChannelOption.SO_KEEPALIVE, NettyConfig.TCP_SO_KEEPALIVE_DEFAULT);

        this.serverBootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

        this.serverBootstrap
                .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(NettyConfig.NETTY_BUFFER_LOW_WATERMARK_DEFAULT, NettyConfig.NETTY_BUFFER_HIGH_WATERMARK_DEFAULT));
    }

    private void stop()
    {
        log.warn(">>> Stop Netty server..............");
        if (started.get())
        {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }
}
