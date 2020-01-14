package com.xiao.springcloud.demo.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;

/**
 * [简要描述]: 字符串长度计算
 * [详细描述]:
 *
 * @author xiaolinlin
 * @version 1.0, 2020/1/14 15:50
 * @since JDK 1.8
 */
@Slf4j
public class StringLengthUtils
{
    private static final int UNICODE_LENGTH = 3;

    private static final int GBK_LENGTH = 2;

    private static final String GBK = "GBK";

    private static final String UTF_8 = "UTF-8";

    /**
     * [简要描述]:获取字符长度<br/>
     * [详细描述]:<br/>
     * 1.如果字符串为空null或""返回0<br/>
     * 2.如果code为空，返回string.length<br/>
     * 3.如果code编码格式不对，返回string.length<br/>
     * 4.否则返回 string.getbyte(code).length<br/>
     *
     * @param st : 字符串
     * @param code : 编码格式
     * @return int
     * xiaolinlin  2020/1/14 - 15:54
     **/
    public static int getLength(String st, String code)
    {
        int length = 0;
        if (StringUtils.isNotBlank(st))
        {
            if (StringUtils.isNotBlank(code))
            {
                try
                {
                    length = st.getBytes(code).length;
                }
                catch (UnsupportedEncodingException e)
                {
                    log.error("编码格式不对：", e);
                }
            }

            if (0 == length)
            {
                length = st.length();
            }
        }
        return length;
    }

    /**
     * [简要描述]:获取utf-8格式下的长度<br/>
     * [详细描述]:<br/>
     *
     * @param st :
     * @return int
     * xiaolinlin  2020/1/14 - 15:59
     **/
    public static int getLengthByUtf(String st)
    {
        return getLength(st, UTF_8);
    }

    /**
     * [简要描述]:获取gbk编码格式下的长度<br/>
     * [详细描述]:<br/>
     *
     * @param str :
     * @return int
     * xiaolinlin  2020/1/14 - 15:59
     **/
    public static int getLengthByGbk(String str)
    {
        return getLength(str, GBK);
    }

    /**
     * [简要描述]:utf8截取指定长度字符串<br/>
     * [详细描述]:<br/>
     * 1. str为null或"",返回"" <br/>
     * 2. length==0,返回str <br/>
     * 3. length > str.getBytes(UTF-8).length,返回str <br/>
     * 4. 正常截取 <br/>
     *
     * @param str : 带截取的字符串
     * @param length : 截取长度
     * @return java.lang.String
     * xiaolinlin  2020/1/14 - 16:38
     **/
    public static String subByUtf8(String str, int length)
    {
        if (StringUtils.isBlank(str))
        {
            return "";
        }

        if (0 == length)
        {
            return str;
        }

        String subStr = "";
        try
        {
            byte[] buf = str.getBytes(UTF_8);
            if (length > buf.length)
            {
                return str;
            }

            int count = 0;
            int i = 0;
            for (i = length - 1; i >= 0; i--)
            {
                if (buf[i] < 0)
                {
                    count++;
                }

            }
            //因為UTF-8三個字節表示一個漢字
            if (count % UNICODE_LENGTH == 0)
            {
                subStr = new String(buf, 0, length, UTF_8);
            }
            else if (count % UNICODE_LENGTH == 1)
            {
                subStr = new String(buf, 0, length - 1, UTF_8);
            }
            else if (count % UNICODE_LENGTH == GBK_LENGTH)
            {
                subStr = new String(buf, 0, length - 2, UTF_8);
            }
        }
        catch (UnsupportedEncodingException e)
        {
            log.error("UTF-8截取字符串长度失败，不支持的编码格式!");
        }
        return subStr;
    }

    /**
     * [简要描述]:utf8截取指定长度字符串<br/>
     * [详细描述]:<br/>
     * 1. str为null或"",返回"" <br/>
     * 2. length==0,返回str <br/>
     * 3. length > str.getBytes(GBK).length,返回str <br/>
     * 4. 正常截取 <br/>
     *
     * @param str : 带截取的字符串
     * @param length : 截取长度
     * @return java.lang.String
     * xiaolinlin  2020/1/14 - 16:38
     **/
    public static String subByGbk(String str, int length)
    {
        if (StringUtils.isBlank(str))
        {
            return "";
        }

        if (0 == length)
        {
            return str;
        }

        String subStr = "";
        try
        {
            byte[] buf = str.getBytes(GBK);

            if (length > buf.length)
            {
                return str;
            }

            int count = 0;
            int i = 0;
            for (i = length - 1; i >= 0; i--)
            {
                if (buf[i] < 0)
                {
                    count++;
                }
                else
                {
                    break;
                }
            }
            //因為GBK用兩個字節表示一個漢字
            if (count % GBK_LENGTH == 0)
            {
                subStr = new String(buf, 0, length, GBK);
            }
            else
            {
                subStr = new String(buf, 0, length - 1, GBK);
            }
        }
        catch (UnsupportedEncodingException e)
        {
            log.error("不支持的编码格式!");
        }
        return subStr;
    }

    public static void main(String[] args) throws Exception
    {
        String str = "新春礼盒（新生礼盒）2 459x394x60mm 组合套装";

        String gbkStr = subByGbk(str, 40);
        System.out.println(gbkStr);

        String utfStr = subByUtf8(str, 40);
        System.out.println(utfStr);

        System.out.println(StringLengthUtils.getLengthByGbk(gbkStr));
        System.out.println(StringLengthUtils.getLengthByUtf(utfStr));
    }
}
