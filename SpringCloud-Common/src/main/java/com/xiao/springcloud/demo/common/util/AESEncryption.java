/*
 * Smile Software Technologies Co., Ltd. Copyright 2015-2017, All rights reserved.
 * 文件名  :AESEncryption.java
 * 创建人  :xiaolinlin
 * 创建时间:2017年3月13日
 */

package com.xiao.springcloud.demo.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;

/**
 * [简要描述]:AES加解密类<br/>
 * [详细描述]:美国软件出口限制，JDK默认使用的AES算法最高只能支持128位。如需要更高的支持需要从oracle官网下载更换JAVA_HOME/jre/lib/
 * security目录下的： local_policy.jar和US_export_policy.jar。<br/>
 * 采用补码方式以及base64双重加密，依赖commons-codec-1.x.jar包中的base64加密<br/>
 *
 * @author xiaolinlin
 * @version 1.0, 2017年3月13日
 * @since smile V100R001C00
 */
@Slf4j
public class AESEncryption
{

    /**
     * 加密器
     */
    private static Cipher cipher;

    /**
     * 初始化向量
     */
    private static IvParameterSpec iv;

    static
    {
        try
        {
            byte[] vi = Hex.decodeHex("12345678123456781234567812345678".toCharArray());
            iv = new IvParameterSpec(vi);
            // "算法/模式/补码方式"
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        }
        catch (NoSuchAlgorithmException e)
        {
            log.error("NoSuchAlgorithmException");
        }
        catch (NoSuchPaddingException e)
        {
            log.error("NoSuchPaddingException");
        }
        catch (DecoderException e)
        {
            log.error("DecoderException");
        }
    }

    /**
     * 生成密钥
     *
     * @param type AES长度
     * @return 密钥
     */
    public static String createAESKey(AESType type)
    {
        try
        {
            KeyGenerator key = KeyGenerator.getInstance("AES");
            key.init(type.value);
            SecretKey ckey = key.generateKey();
            byte[] keyByte = ckey.getEncoded();
            return ByteUtils.parseByte2HexStr(keyByte);
        }
        catch (NoSuchAlgorithmException e)
        {
            log.error("Create aes Key error!");
        }
        return "";
    }

    /**
     * AES加密
     *
     * @param key 密钥
     * @param plaintext 明文
     * @return 秘文
     */
    public static String encryptAES(String key, String plaintext)
    {
        String ciphertext = "";
        try
        {
            byte[] keyByte = ByteUtils.parseHexStr2Byte(key);
            SecretKeySpec skeySpec = new SecretKeySpec(keyByte, "AES");
            // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] pbyte = plaintext.getBytes("utf-8");
            byte[] result = cipher.doFinal(pbyte);
            ciphertext = ByteUtils.parseByte2HexStr(result);
        }
        catch (Exception e)
        {
            log.error("Aes encrypt error", e);
        }
        return ciphertext;
    }

    /**
     * AES解密
     *
     * @param key 密钥
     * @param ciphertext 秘文
     * @return 明文
     */
    public static String decryptAES(String key, String ciphertext)
    {
        String plaintext = "";
        try
        {
            byte[] keyByte = ByteUtils.parseHexStr2Byte(key);
            byte[] cbyte = ByteUtils.parseHexStr2Byte(ciphertext);
            SecretKeySpec skeySpec = new SecretKeySpec(keyByte, "AES");
            // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] pbyte = cipher.doFinal(cbyte);
            plaintext = new String(pbyte, "utf-8");
        }
        catch (Exception e)
        {
            log.error("AES decrypt error!", e);
        }
        return plaintext;
    }

    public static void main(String[] args)
    {
        String key = AESEncryption.createAESKey(AESType.AES_128);
        System.out.println(key);
        String ciphertext = AESEncryption.encryptAES(key, "Basisuser123");
        System.out.println(ciphertext);
        System.out.println(AESEncryption.decryptAES(key, ciphertext));
    }
}
