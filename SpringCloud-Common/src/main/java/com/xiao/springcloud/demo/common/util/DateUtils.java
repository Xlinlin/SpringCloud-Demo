package com.xiao.springcloud.demo.common.util;

import java.util.Calendar;
import java.util.Date;

/**
 * [简要描述]: 日期工具类
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/11/6 16:52
 * @since JDK 1.8
 */
public class DateUtils
{
    /**
     * [简要描述]:获取下一天的0点时间<br/>
     * [详细描述]:<br/>
     *
     * @return long
     * llxiao  2019/11/5 - 23:46
     **/
    public static Date getNextDay()
    {
        //得到一个Calendar实例
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 24);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Date(calendar.getTimeInMillis());
    }
}
