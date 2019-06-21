/*
 * Smile Software Technologies Co., Ltd. Copyright 2015-2017, All rights reserved.
 * 文件名  :BinaryHelper.java
 * 创建人  :xiaolinlin
 * 创建时间:2017年2月20日
*/

package com.xiao.springcloud.demo.common.util;

import org.apache.commons.codec.binary.Base64;

/**
 * [简要描述]:二进制转换工具类<br/>
 * [详细描述]:二进制转16进制字符串、二进制转apache的base64字符串，字符串转二进制<br/>
 *
 * @author xiaolinlin
 * @version 1.0, 2017年2月20日
 * @since smile V100R001C00
 */
public class BinaryHelper
{
    /**
     * [简要描述]:二进制转字符串<br/>
     * [详细描述]:二进制转字符串<br/>
     * 
     * @author xiaolinlin
     * @param array 二进制数组
     * @return 字符串
     */
    public static String array2Str(byte[] array)
    {
        if (null == array)
        {
            return "";
        }
        StringBuffer sb = new StringBuffer(32);
        for (int i = 0; i < array.length; i++)
        {
            sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).toUpperCase().substring(1, 3));
        }
        return sb.toString();
    }

    /**
     * [简要描述]:二进制转base64字符串<br/>
     * [详细描述]:二进制转base64字符串<br/>
     * 
     * @author xiaolinlin
     * @param array 二进制数组
     * @return base64字符串
     */
    public static String array2Base64Str(byte[] array)
    {
        return Base64.encodeBase64String(array);
    }

    /**
     * [简要描述]:符合apache commons 格式的base64字符串转数组<br/>
     * [详细描述]:<br/>
     * 
     * @author xiaolinlin
     * @param base64Str 符合apache commons 格式的base64字符串
     * @return 二进制数组
     */
    public static byte[] base642Array(String base64Str)
    {
        return Base64.decodeBase64(base64Str);
    }
}
