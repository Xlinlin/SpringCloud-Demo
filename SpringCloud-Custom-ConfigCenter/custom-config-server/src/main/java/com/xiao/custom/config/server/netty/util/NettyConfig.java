package com.xiao.custom.config.server.netty.util;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/3/30 14:57
 * @since JDK 1.8
 */
public class NettyConfig
{
    public static final int NETTY_IO_RATIO_DEFAULT = 70;

    public static final int TCP_SO_BACKLOG_DEFAULT = 128;

    public static final boolean TCP_SO_REUSEADDR_DEFAULT = true;

    public static final boolean TCP_NODELAY_DEFAULT = true;

    public static final boolean TCP_SO_KEEPALIVE_DEFAULT = true;

    /**
     * low.watermark默认大小为8388608，即8M；
     * high.watermark默认大小为16777216，即16M
     * 两个参数控制的是Channel.isWritable()方法
     * https://www.jianshu.com/p/a1166c34ae46
     */
    public static final Integer NETTY_BUFFER_HIGH_WATERMARK_DEFAULT = 64 * 1024;
    public static final Integer NETTY_BUFFER_LOW_WATERMARK_DEFAULT = 32 * 1024;
}
