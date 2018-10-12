package com.xiao.skywalking.demo.common.cache.code;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.redisson.client.codec.Codec;
import org.redisson.client.handler.State;
import org.redisson.client.protocol.Decoder;
import org.redisson.client.protocol.Encoder;

import java.io.IOException;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author zhdong
 * @version 1.0,  2018/9/20
 * @since JDK 1.8
 */
@Slf4j
public class FastJsonCodec implements Codec
{

    public static final FastJsonCodec INSTANCE = new FastJsonCodec();
    private Encoder encoder = null;
    private Decoder<Object> decoder = null;
    private Encoder keyEncoder = null;
    private Decoder<Object> keyDecoder = null;

    public FastJsonCodec()
    {
        this.encoder = new Encoder()
        {
            public ByteBuf encode(Object in) throws IOException
            {
                ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
                try
                {
                    SerializerObject serial = new SerializerObject(in);
                    ByteBufOutputStream os = new ByteBufOutputStream(out);

                    JSON.writeJSONString(os, serial, SerializerFeature.WriteMapNullValue,  //是否输出值为null的字段,默认为false
                            SerializerFeature.DisableCircularReferenceDetect, //消除循环引用
                            SerializerFeature.WriteClassName,//写入类名便于序列化解析
                            SerializerFeature.WriteNullStringAsEmpty);
                    return os.buffer();
                }
                catch (IOException var4)
                {
                    out.release();
                    throw var4;
                }
            }
        };
        this.decoder = new Decoder<Object>()
        {
            public Object decode(ByteBuf buf, State state) throws IOException
            {
                SerializerObject serial = JSON.parseObject(new ByteBufInputStream(buf), SerializerObject.class);
                return serial.getValue();
            }
        };

        this.keyEncoder = new Encoder()
        {
            public ByteBuf encode(Object in) throws IOException
            {
                ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
                try
                {
                    ByteBufOutputStream os = new ByteBufOutputStream(out);
                    JSON.writeJSONString(os, in);
                    return os.buffer();
                }
                catch (IOException var4)
                {
                    out.release();
                    throw var4;
                }
            }
        };

        this.keyDecoder = new Decoder<Object>()
        {
            public Object decode(ByteBuf buf, State state) throws IOException
            {
                return JSON.parseObject(new ByteBufInputStream(buf), String.class);
            }
        };

    }

    @Override
    public Decoder<Object> getMapValueDecoder()
    {
        return decoder;
    }

    @Override
    public Encoder getMapValueEncoder()
    {
        return encoder;
    }

    @Override
    public Decoder<Object> getMapKeyDecoder()
    {
        return keyDecoder;
    }

    @Override
    public Encoder getMapKeyEncoder()
    {
        return keyEncoder;
    }

    @Override
    public Decoder<Object> getValueDecoder()
    {
        return decoder;
    }

    @Override
    public Encoder getValueEncoder()
    {
        return encoder;
    }


}
