/*
 * Smile Software Technologies Co., Ltd. Copyright 2015-2017, All rights reserved.
 * 文件名  :HMACUtil.java
 * 创建人  :xiaolinlin
 * 创建时间:2017年4月20日
 */

package com.xiao.springcloud.demo.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * [简要描述]:hamc加密算法<br/>
 * [详细描述]:<br/>
 *
 * @author xiaolinlin
 * @version 1.0, 2017年4月20日
 * @since smile V100R001C00
 */
@Slf4j
public class HMACUtil
{

    /**
     * HmacSHA1
     */
    private static final String HMAC_SHA1 = "HmacSHA1";

    /**
     * HmacSHA256
     */
    private static final String HMAC_SHA256 = "HmacSHA256";

    /**
     * [简要描述]:hmacSHA1加密算法，加密后的密文为 apache base64字符串<br/>
     * [详细描述]:<br/>
     *
     * @param plainText 明文
     * @param key 密钥
     * @return 密文
     */
    public static String hmacSHA1Encode(String plainText, String key)
    {
        return hmacEncode(plainText, key, HMAC_SHA1);
    }

    /**
     * [简要描述]:hmacSHA1加密算法，加密后的密文为 apache base64字符串<br/>
     * [详细描述]:<br/>
     *
     * @param plainText 明文
     * @param key 密钥
     * @return 密文
     */
    public static String hmacSHA256Encode(String plainText, String key)
    {
        return hmacEncode(plainText, key, HMAC_SHA256);
    }

    /**
     * [简要描述]:hmac加密算法<br/>
     * [详细描述]:<br/>
     *
     * @param plainText 明文
     * @param key 密钥
     * @param shaByte 加密算法，如HmacSHA1、HmacSHA256
     * @return 加密后的密文为 apache base64字符串
     */
    private static String hmacEncode(String plainText, String key, String shaByte)
    {
        String ciphertext = "";
        try
        {
            SecretKeySpec signingKey = new SecretKeySpec(key
                    .getBytes(CodeFormatConstants.CODE_FORMAT_UTF_8), HMAC_SHA1);
            Mac mac = Mac.getInstance(shaByte);
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(plainText.getBytes(CodeFormatConstants.CODE_FORMAT_UTF_8));
            ciphertext = Base64.encodeBase64String(rawHmac);
        }
        catch (UnsupportedEncodingException e)
        {
            log.error("UnsupportedEncodingException");
        }
        catch (NoSuchAlgorithmException e)
        {
            log.error("NoSuchAlgorithmException");
        }
        catch (InvalidKeyException e)
        {
            log.error("InvalidKeyException", e);
        }
        return ciphertext;
    }
}
