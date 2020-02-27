package com.xiao.springcloud.demo.common.sign.util;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * [简要描述]: 签名工具
 * [详细描述]:
 *
 * @author xiaolinlin
 * @version 1.0, 2020/2/21 11:04
 * @since JDK 1.8
 */
public class SignUtil
{
    private static final Logger logger = LoggerFactory.getLogger(SignUtil.class);

    /**
     * HmacSHA1
     */
    private static final String HMAC_SHA1 = "HmacSHA1";

    /**
     * [简要描述]:键值对参数生产签名串<br/>
     * [详细描述]:<br/>
     *
     * @param kvParams : 兼职对参数
     * @param key :
     * @return java.lang.String
     * xiaolinlin  2020/2/24 - 18:48
     **/
    public static String generateSign(Map<String, String> kvParams, String key)
    {
        if (MapUtils.isNotEmpty(kvParams) && StringUtils.isNotBlank(key))
        {
            StringBuilder paramSb = new StringBuilder();
            kvParams.forEach((k, v) -> paramSb.append(k + v));
            return generateSign(paramSb.toString(), key);
        }
        return "";
    }

    /**
     * [简要描述]:JSON和键值对参数合并<br/>
     * [详细描述]:<br/>
     *
     * @param kvParams :
     * @param jsonParams :
     * @return java.lang.String
     * xiaolinlin  2020/2/24 - 18:50
     **/
    public static String generateSign(Map<String, String> kvParams, String jsonParams, String key)
    {
        if (StringUtils.isNotBlank(key))
        {
            StringBuilder paramSb = new StringBuilder();
            if (MapUtils.isNotEmpty(kvParams))
            {
                kvParams.forEach((k, v) -> paramSb.append(k + v));
            }
            if (StringUtils.isNotBlank(jsonParams))
            {
                try
                {
                    final JSONObject jsonObject = JSONObject.parseObject(jsonParams);
                    paramSb.append(jsonObject.toJSONString());
                }
                catch (JSONException e)
                {
                    logger.error("生成签名串的JSON请求参数错误，不能转成JSON格式!", e);
                }
            }
            return generateSign(paramSb.toString(), key);
        }

        return "";
    }

    /**
     * [简要描述]:json参数处理<br/>
     * [详细描述]:<br/>
     *
     * @param jsonParams :
     * @param key :
     * @return java.lang.String
     * xiaolinlin  2020/2/24 - 18:50
     **/
    public static String generateSignByJson(String jsonParams, String key)
    {
        if (StringUtils.isNotBlank(jsonParams))
        {

            try
            {
                final JSONObject jsonObject = JSONObject.parseObject(jsonParams);
                return generateSign(jsonObject.toJSONString(), key);
            }
            catch (JSONException e)
            {
                logger.error("生成签名串的JSON请求参数错误，不能转成JSON格式!", e);
            }
        }
        return "";
    }

    /**
     * [简要描述]:生成签名串<br/>
     * [详细描述]:<br/>
     *
     * @param content : 原始内容
     * @param key : 签名key
     * @return java.lang.String
     * xiaolinlin  2020/2/21 - 11:05
     **/
    public static String generateSign(String content, String key)
    {
        if (StringUtils.isBlank(content) || StringUtils.isBlank(key))
        {
            return "";
        }
        final String sort = AsciiSortUtil.sort(content);
        return hmacEncode(sort, key);
    }

    /**
     * [简要描述]:hmac加密算法<br/>
     * [详细描述]:<br/>
     *
     * @param plainText 明文
     * @param key 密钥
     * @return 加密后的密文为 apache base64字符串
     */
    private static String hmacEncode(String plainText, String key)
    {
        Charset charset = StandardCharsets.UTF_8;
        Mac mac;
        String sign = "";
        try
        {
            mac = Mac.getInstance(HMAC_SHA1);
            mac.init(new SecretKeySpec(key.getBytes(charset), HMAC_SHA1));
            byte[] bytes = mac.doFinal(plainText.getBytes(charset));
            sign = new String(Base64.encodeBase64(bytes), charset);
        }
        catch (NoSuchAlgorithmException | InvalidKeyException e)
        {
            logger.error("macSignature error");
        }

        return sign;
    }

    public static void main(String[] args)
    {
        String json = "{\t\"storeNo\":\"1001\",\t\"productDtoList\":[\t{\t\"id\":\"12\",\t\"stock\":12,\t\"shopCode\":\"J001\",\t\"productCode\":\"\"\t},\t{\t\"id\":\"12\",\t\"shopCode\":\"J001\",\t\"productCode\":\"\"\t}\t]}";
        Map<String, String> kvParmas = new HashMap<>();
        kvParmas.put("productName", "test11111");
        String key = "abcdefg111";
        System.out.println(generateSign(kvParmas, key));
        System.out.println(generateSign(kvParmas, json, key));
        System.out.println(generateSignByJson(json, key));
    }

}
