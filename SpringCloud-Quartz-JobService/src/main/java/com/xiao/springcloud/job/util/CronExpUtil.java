package com.xiao.springcloud.job.util;

import org.apache.commons.lang3.StringUtils;
import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.impl.triggers.CronTriggerImpl;

import java.text.ParseException;
import java.util.Date;

/**
 * [简要描述]: quartz表达式校验
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/7/2 18:52
 * @since JDK 1.8
 */
public class CronExpUtil
{
    /**
     * [简要描述]:Quartz 表达式校验<br/>
     * [详细描述]:<br/>
     * 1. 校验表达式格式</p>
     * 2. 校验表达式合法性，以及是否可用</p>
     *
     * @param cronExp :
     * @return boolean
     * llxiao  2019/7/2 - 18:53
     **/
    public static boolean validator(String cronExp)
    {
        boolean flag = false;
        if (StringUtils.isNotEmpty(cronExp))
        {
            // 校验表达式的格式是否正确
            flag = CronExpression.isValidExpression(cronExp);
            if (flag)
            {
                CronTrigger cronTrigger = new CronTriggerImpl();
                try
                {
                    // 校验 表达式是否有效。比如过期的，失效的等
                    ((CronTriggerImpl) cronTrigger).setCronExpression(cronExp);
                    Date date = ((CronTriggerImpl) cronTrigger).computeFirstFireTime(null);
                    flag = date != null && date.after(new Date());
                }
                catch (ParseException e)
                {
                    flag = false;
                }
            }
        }
        return flag;
    }
}
