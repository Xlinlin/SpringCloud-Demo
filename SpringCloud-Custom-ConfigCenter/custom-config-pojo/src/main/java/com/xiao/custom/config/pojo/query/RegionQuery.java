package com.xiao.custom.config.pojo.query;

import com.xiao.custom.config.pojo.common.BaseQuery;
import lombok.Data;

import java.util.Date;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author jun.liu
 * @version 1.0, 2018/11/26 17:24
 * @since JDK 1.8
 */
@Data
public class RegionQuery extends BaseQuery
{
    //区域名称
    private String regionName;
    //开始时间
    private Date createTime;
    //结束时间
    private Date updateTime;
}
