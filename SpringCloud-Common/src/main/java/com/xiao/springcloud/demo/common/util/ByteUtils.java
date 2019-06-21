/*
 * Smile Software Technologies Co., Ltd. Copyright 2015-2017, All rights reserved.
 * 文件名  :ByteUtils.java
 * 创建人  :xiaolinlin
 * 创建时间:2017年6月9日
 */

package com.xiao.springcloud.demo.common.util;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author xiaolinlin
 * @version 1.0, 2017年6月9日
 * @since smile V100R001C00
 */
public class ByteUtils
{
    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr)
    {
        if (hexStr.length() < 1)
        {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++)
        {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[])
    {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++)
        {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1)
            {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

}
