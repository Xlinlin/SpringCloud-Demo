package com.xiao.custom.config.client.netty.coder;

import com.xiao.custom.config.client.netty.util.ProtostuffUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * [简要描述]: 解码
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/3/30 13:54
 * @since JDK 1.8
 */
@ChannelHandler.Sharable
public class ProtoEncoder extends MessageToByteEncoder<Object>
{
    private Class<?> genericClass;

    public ProtoEncoder(Class cls)
    {
        this.genericClass = cls;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object msg, ByteBuf byteBuf) throws Exception
    {
        // 序列化
        if (genericClass.isInstance(msg))
        {
            byte[] data = ProtostuffUtil.serialize(msg);
            byteBuf.writeInt(data.length);
            byteBuf.writeBytes(data);
        }
    }
}
