/*
 * Smile Software Technologies Co., Ltd. Copyright 2015-2017, All rights reserved.
 * 文件名  :Md5DigestUtil.java
 * 创建人  :xiaolinlin
 * 创建时间:2017年2月20日
 */

package com.xiao.springcloud.demo.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * [简要描述]:md5工具类<br/>
 * [详细描述]:<br/>
 *
 * @author xiaolinlin
 * @version 1.0, 2017年2月20日
 * @since smile V100R001C00
 */
@Slf4j
public class Md5DigestUtil
{

    /**
     * MD5摘要算法
     */
    private static final String MD5 = "MD5";

    /**
     * [简要描述]:md5加密算法，生成base64字符串<br/>
     * [详细描述]:<br/>
     *
     * @param plaintext 明文
     * @return md5密文
     */
    public static String md5Encoding2Base64(String plaintext)
    {
        return md5Encoding(plaintext, true);
    }

    /**
     * [简要描述]:md5加密算法，生成普通字符串<br/>
     * [详细描述]:<br/>
     *
     * @param plaintext 明文
     * @return md5密文
     */
    public static String md5Encoding(String plaintext)
    {
        return md5Encoding(plaintext, false);
    }

    /**
     * [简要描述]:MD5摘要生成工具<br/>
     * [详细描述]:MD5摘要生成工具<br/>
     *
     * @param plaintext 明文
     * @param isBase64 是否生成base64字符串
     * @return md5密文
     */
    public static String md5Encoding(String plaintext, boolean isBase64)
    {
        if (StringUtils.isBlank(plaintext))
        {
            return "";
        }
        String ciphertext = "";
        MessageDigest md;
        try
        {
            md = MessageDigest.getInstance(MD5);
            byte[] array = md.digest(plaintext.getBytes(CodeFormatConstants.CODE_FORMAT_UTF_8));
            if (isBase64)
            {
                ciphertext = BinaryHelper.array2Base64Str(array);
            }
            else
            {
                ciphertext = BinaryHelper.array2Str(array);
            }
        }
        catch (NoSuchAlgorithmException e)
        {
            log.error("MD5 encoding error and error msg :NoSuchAlgorithmException!");
        }
        catch (UnsupportedEncodingException e)
        {
            log.error("MD5 encoding error and error msg :UnsupportedEncodingException!");
        }
        return ciphertext;
    }

}
