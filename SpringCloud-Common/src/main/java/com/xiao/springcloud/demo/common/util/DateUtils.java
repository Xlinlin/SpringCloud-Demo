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
     * [简要描述]:获取下一天的0点时间·<br/>
     * [详细描述]:<br/>
     *
     * @return long
     * llxiao  2019/11/5 - 23:46
     **/
    public static Date getNextDay()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar
                .get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * [简要描述]:获取当天最后的时间：23:59:59<br/>
     * [详细描述]:<br/>
     *
     * @return java.util.Date
     * llxiao  2019/11/7 - 19:49
     **/
    public static Date getDayEnd()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar
                .get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        return calendar.getTime();
    }
}
