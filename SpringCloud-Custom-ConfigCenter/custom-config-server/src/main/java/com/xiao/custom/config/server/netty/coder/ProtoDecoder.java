package com.xiao.custom.config.server.netty.coder;

import com.xiao.custom.config.server.netty.util.ProtostuffUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * [简要描述]: 编码
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/3/30 13:54
 * @since JDK 1.8
 */
public class ProtoDecoder extends ByteToMessageDecoder
{
    private static final int PROTO_BUFF_FLAG = 4;

    private Class<?> genericClass;

    public ProtoDecoder(Class cls)
    {
        this.genericClass = cls;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception
    {
        if (byteBuf.readableBytes() < PROTO_BUFF_FLAG)
        {
            return;
        }
        byteBuf.markReaderIndex();
        int dataLength = byteBuf.readInt();
        if (dataLength < 0)
        {
            ctx.close();
        }
        if (byteBuf.readableBytes() < dataLength)
        {
            byteBuf.resetReaderIndex();
        }
        // 将ByteBuf转换为byte[]
        byte[] data = new byte[dataLength];
        byteBuf.readBytes(data);
        // 将data转换成object
        Object obj = ProtostuffUtil.deserialize(data, genericClass);
        list.add(obj);
    }
}
