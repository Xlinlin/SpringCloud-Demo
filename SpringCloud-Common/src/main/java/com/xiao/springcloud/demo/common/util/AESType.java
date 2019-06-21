/*
 * Smile Software Technologies Co., Ltd. Copyright 2015-2017, All rights reserved.
 * 文件名  :AESType.java
 * 创建人  :xiaolinlin
 * 创建时间:2017年3月13日
 */

package com.xiao.springcloud.demo.common.util;

/**
 * [简要描述]:AES加密类型枚举<br/>
 * [详细描述]:<br/>
 *
 * @author xiaolinlin
 * @version 1.0, 2017年3月13日
 * @since smile V100R001C00
 */
public enum AESType
{
    AES_128(128),
    AES_192(192),
    AES_256(256);

    /**
     * 初始化值
     */
    public int value;

    AESType(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        this.value = value;
    }

}
