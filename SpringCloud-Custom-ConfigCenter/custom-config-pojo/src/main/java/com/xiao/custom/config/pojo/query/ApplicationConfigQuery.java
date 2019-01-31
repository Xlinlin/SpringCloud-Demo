package com.xiao.custom.config.pojo.query;

import com.xiao.custom.config.pojo.common.BaseQuery;
import lombok.Data;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/1/7 15:18
 * @since JDK 1.8
 */
@Data
public class ApplicationConfigQuery extends BaseQuery
{
    private Long applicationId;
    private String itemKey;
    private String itemDesc;
}
