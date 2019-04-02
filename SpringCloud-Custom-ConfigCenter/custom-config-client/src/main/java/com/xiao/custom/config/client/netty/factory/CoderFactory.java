package com.xiao.custom.config.client.netty.factory;

import com.xiao.custom.config.client.netty.coder.ProtoDecoder;
import com.xiao.custom.config.client.netty.coder.ProtoEncoder;
import com.xiao.custom.config.client.netty.dto.Message;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/3/30 14:31
 * @since JDK 1.8
 */
public class CoderFactory
{
    public static ProtoDecoder newDecoder()
    {
        return new ProtoDecoder(Message.class);
    }

    public static ProtoEncoder newEncoder()
    {
        return new ProtoEncoder(Message.class);
    }
}
