/*
 * Winner
 * 文件名  :Validator.java
 * 创建人  :llxiao
 * 创建时间:2017年12月27日
 */

package com.xiao.springcloud.demo.common.validator;

import com.xiao.springcloud.demo.common.exception.CommonException;
import com.xiao.springcloud.demo.common.exception.CommonExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

/**
 * [简要描述]:参数校验器<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2017年12月27日
 * @since JDK1.8
 */
@Slf4j
public class ParamValidator
{
    /**
     * [简要描述]:校验对象属性<br/>
     * [详细描述]:对象的属性用ParamVerify注解校验<br/>
     *
     * @param object 校验对象
     */
    public static void validator(Object object)
    {
        if (null == object)
        {
            return;
        }

        final Class<?> objClass = object.getClass();
        //获取业务编码前缀
        CodePrefix codePrefix = objClass.getAnnotation(CodePrefix.class);
        int moduleCode = 0;
        if (null != codePrefix)
        {
            moduleCode = codePrefix.moduleCode();
        }
        try
        {
            // 获取所有字段 包括private 不包括父类字段
            Field[] farray = objClass.getDeclaredFields();
            // 初始化校验注解
            Class<ParamVerify> verify = ParamVerify.class;
            String fieldName;
            ParamVerify paramVerify;
            String reg;
            String fValue;
            for (Field field : farray)
            {
                // 获取其中字段
                fieldName = field.getName();
                // 判断是否被chkstring注解所标识
                if (field.isAnnotationPresent(verify))
                {
                    // 返回这个类所标识的注解对象
                    paramVerify = field.getAnnotation(verify);

                    // 可以为空
                    boolean canBlank = paramVerify.canBlank();
                    // 设置权限
                    field.setAccessible(true);

                    // 获取属性值
                    fValue = String.valueOf(field.get(object));
                    if (!canBlank)
                    {
                        // 必填校验
                        if (StringUtils.isBlank(fValue))
                        {
                            log.info("缺少:{}必填参数", fieldName);
                            // 如果字段是为空
                            throw new CommonException(generateCode(moduleCode, CommonException.REQUIRED_PARAM_SUFFIX), String
                                    .format("缺少:%s必填参数", fieldName));
                        }

                        int maxLeng = paramVerify.maxLeng();
                        // 参数最大长度校验
                        if (0 != maxLeng && fValue.length() > maxLeng)
                        {
                            log.info("求参数:{}过长", fieldName);
                            throw new CommonException(generateCode(moduleCode, CommonException.ILLEGAL_PARAM_SUFFIX), String
                                    .format("参数:%s非法", fieldName));
                        }

                        // 正则校验
                        reg = paramVerify.regex();
                        if (StringUtils.isNotBlank(reg) && !fValue.matches(reg))
                        {
                            log.info("请求参数:{}非法", fieldName);
                            throw new CommonException(generateCode(moduleCode, CommonException.ILLEGAL_PARAM_SUFFIX), String
                                    .format("参数:%s非法", fieldName));
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            log.error("系统内部错误", e);
            throw new CommonException(CommonExceptionEnum.SYSTEM_ERROR);
        }
    }

    private static int generateCode(int moduleCode, String serviceCode)
    {
        String errorCode = moduleCode + serviceCode;
        return Integer.parseInt(errorCode);
    }
}
