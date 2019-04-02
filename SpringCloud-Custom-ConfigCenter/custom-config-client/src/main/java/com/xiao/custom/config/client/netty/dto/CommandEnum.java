package com.xiao.custom.config.client.netty.dto;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/3/30 16:18
 * @since JDK 1.8
 */
public enum CommandEnum
{
    /**
     * 心跳
     */
    IDLE(0),
    /**
     * 刷新
     */
    REFRESH(2),
    /**
     * 登录，绑定信息
     */
    LOGIN(1);

    int status;

    CommandEnum(int status)
    {
        this.status = status;
    }

    public int getStatus()
    {
        return status;
    }
}
