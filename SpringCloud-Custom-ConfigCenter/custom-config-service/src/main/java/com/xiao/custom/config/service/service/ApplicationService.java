package com.xiao.custom.config.service.service;

import com.github.pagehelper.PageInfo;
import com.xiao.custom.config.pojo.dto.ApplicationConfigDto;
import com.xiao.custom.config.pojo.dto.ApplicationDto;
import com.xiao.custom.config.pojo.entity.Application;
import com.xiao.custom.config.pojo.entity.ApplicationConfig;
import com.xiao.custom.config.pojo.query.AppQuery;
import com.xiao.custom.config.pojo.query.ApplicationConfigQuery;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author jun.liu
 * @version 1.0, 2018/11/26 16:47
 * @since JDK 1.8
 */
public interface ApplicationService
{
    /**
     * [简要描述]:保存<br/>
     * [详细描述]:<br/>
     *
     * @param applicationConfig :
     * @return java.lang.Integer
     * jun.liu  2018/11/26 - 16:52
     **/
    Integer save(Application applicationConfig, String[] groupIdArr);

    Integer update(Application applicationConfig);

    Integer delete(Long id);

    PageInfo<ApplicationDto> pageApplicationConfig(AppQuery appQuery, Integer pageNum, Integer pageSize);

    /**
     * [简要描述]:通过id获取<br/>
     * [详细描述]:<br/>
     *
     * @param id :
     * @return com.winner.config.center.pojo.db.entity.ApplicationConfig
     * jun.liu  2018/11/28 - 14:50
     **/
    Application selectApplicationConfigById(Long id);

    /**
     * [简要描述]:分页查询应用关联的私有属性<br/>
     * [详细描述]:<br/>
     *
     * @param query :
     * @return java.util.List<com.winner.config.center.pojo.db.dto.ApplicationConfigDto>
     * llxiao  2019/1/7 - 15:27
     **/
    PageInfo<ApplicationConfigDto> pageQuery(ApplicationConfigQuery query);

    /**
     * [简要描述]:保存应用的私有配置信息<br/>
     * [详细描述]:<br/>
     *
     * @param applicationConfig :
     * @return boolean
     * llxiao  2019/1/7 - 16:56
     **/
    boolean saveApplicationConfig(ApplicationConfig applicationConfig);

    /**
     * [简要描述]:主键更新<br/>
     * [详细描述]:<br/>
     *
     * @param applicationConfig :
     * @return boolean
     * llxiao  2019/1/7 - 16:58
     **/
    boolean updateApplicationConfig(ApplicationConfig applicationConfig);

    /**
     * [简要描述]:删除私有属性<br/>
     * [详细描述]:<br/>
     *
     * @param id :
     * @return boolean
     * llxiao  2019/1/8 - 9:21
     **/
    boolean delPrivateConfig(Long id);

    /**
     * [简要描述]:全部发布配置<br/>
     * [详细描述]:<br/>
     *
     * @param id :
     * @return boolean
     * llxiao  2019/1/30 - 10:53
     **/
    boolean refresh(Long id);

    /**
     * [简要描述]:指定客户端按户型配置<br/>
     * [详细描述]:<br/>
     *
     * @param hostInfoIds : 客户端信息
     * @return boolean
     * llxiao  2019/3/27 - 14:06
     **/
    boolean batchRefresh(Long... hostInfoIds);
}
