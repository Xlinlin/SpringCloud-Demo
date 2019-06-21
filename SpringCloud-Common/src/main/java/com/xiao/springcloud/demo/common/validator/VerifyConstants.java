/*
 * Winner
 * 文件名  :VerifyConstants.java
 * 创建人  :llxiao
 * 创建时间:2017年12月27日
 */

package com.xiao.springcloud.demo.common.validator;

/**
 * [简要描述]:验证正则表达式<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2017年12月27日
 * @since JDK1.8
 */
public interface VerifyConstants
{

    /**
     * 电话校验
     */
    String TELL_REG = "^((0|\\+)?\\d{2})?1[356789]\\d{9}|([0\\+]\\d{2,3}-)?(0\\d{2,3}-)(\\d{7,8})(-(\\d{3,}))?$";

    /**
     * 营业时间校验
     */
    String BUSINESS_HOURS = "^((20|21|22|23|[0-1]*\\d)\\:[0-5][0-9])$";

    /**
     * ID校验
     */
    String ID_REG = "^[0-9]*$";

    /**
     * 纬度校验<br>
     * <br>
     */
    String LAT_REG = "^-?((0|[1-8]?[0-9]?)(([.][0-9]{1,8})?)|90(([.][0]{1,8})?))$";

    /**
     * 经度校验<br>
     * <br>
     */
    String LON_REG = "^-?((0|1?[0-7]?[0-9]?)(([.][0-9]{1,8})?)|180(([.][0]{1,8})?))$";

    /**
     * 1位数字
     */
    String ONE_NUMBER_REG = "^\\d{1}$";

    /**
     * 数字校验
     */
    String NUMBER_REG = "^\\d{1,2}$";

    /**
     * 中文、英文、数字但不包括下划线等特殊符号。2-64位长度
     */
    String NAME_REG = "^[\\u4E00-\\u9FA5A-Za-z0-9]{2,64}$";

    /**
     * 中文、英文、数字但不包括下划线等特殊符号。2-128位长度
     */
    String NAME_REG_128 = "^[\\u4E00-\\u9FA5A-Za-z0-9]{2,128}$";
}
