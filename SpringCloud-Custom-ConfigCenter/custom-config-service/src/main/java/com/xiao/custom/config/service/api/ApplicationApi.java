package com.xiao.custom.config.service.api;

import com.github.pagehelper.PageInfo;
import com.xiao.custom.config.pojo.dto.ApplicationConfigDto;
import com.xiao.custom.config.pojo.dto.ApplicationDto;
import com.xiao.custom.config.pojo.dto.ConfigItemGroupDto;
import com.xiao.custom.config.pojo.entity.Application;
import com.xiao.custom.config.pojo.entity.ApplicationConfig;
import com.xiao.custom.config.pojo.query.AppQuery;
import com.xiao.custom.config.pojo.query.ApplicationConfigQuery;
import com.xiao.custom.config.pojo.query.ConfigItemGroupQuery;
import com.xiao.custom.config.service.service.ApplicationItemGroupRelationService;
import com.xiao.custom.config.service.service.ApplicationService;
import com.xiao.custom.config.service.service.ConfigItemGroupService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author jun.liu
 * @version 1.0, 2018/11/28 10:18
 * @since JDK 1.8
 */
@RestController
@RequestMapping(value = "/application")
@Slf4j
public class ApplicationApi
{
    @Autowired
    private ApplicationService applicationConfigService;
    @Autowired
    private ConfigItemGroupService configItemGroupService;
    @Autowired
    private ApplicationItemGroupRelationService applicationItemGroupRelationService;

    /**
     * [简要描述]:分页获取<br/>
     * [详细描述]:<br/>
     *
     * @param appQuery :
     * @return com.github.pagehelper.PageInfo<com.winner.config.center.pojo.db.dto.ApplicationConfigDto>
     * jun.liu  2018/11/28 - 10:20
     **/
    @RequestMapping(value = "/page")
    public PageInfo<ApplicationDto> pageApplicationConfig(@RequestBody AppQuery appQuery)
    {
        return applicationConfigService.pageApplicationConfig(appQuery, appQuery.getPageNum(), appQuery.getPageSize());
    }

    /**
     * [简要描述]:新增应用<br/>
     * [详细描述]:<br/>
     *
     * @param applicationConfigDto :
     * @return java.lang.Boolean
     * jun.liu  2018/11/28 - 10:29
     **/
    @RequestMapping(value = "/save")
    public Boolean save(@RequestBody ApplicationDto applicationConfigDto)
    {
        if (StringUtils.isBlank(applicationConfigDto.getApplication()) || StringUtils
                .isBlank(applicationConfigDto.getLabel()) || StringUtils.isBlank(applicationConfigDto.getProfile()))
        {
            log.info("新增应用失败,参数不能为空");
            throw new RuntimeException("参数不能为空");
        }
        applicationConfigDto.setCreateTime(new Date());
        String[] groupIdArr = null;
        if (StringUtils.isNotBlank(applicationConfigDto.getGroupIds()))
        {
            groupIdArr = applicationConfigDto.getGroupIds().split(",");
        }
        int a = applicationConfigService.save(ApplicationDto.convertToEntity(applicationConfigDto), groupIdArr);
        if (a > 0)
        {
            return true;
        }
        return false;
    }

    /**
     * [简要描述]:更新<br/>
     * [详细描述]:<br/>
     *
     * @param applicationConfigDto :
     * @return java.lang.Boolean
     * jun.liu  2018/11/28 - 14:54
     **/
    @RequestMapping(value = "/update")
    public Boolean update(@RequestBody ApplicationDto applicationConfigDto)
    {
        if (null == applicationConfigDto.getId() || StringUtils.isBlank(applicationConfigDto.getApplication())
                || StringUtils.isBlank(applicationConfigDto.getLabel()) || StringUtils
                .isBlank(applicationConfigDto.getProfile()))
        {
            log.info("更新应用失败,参数不能为空");
            throw new RuntimeException("参数不能为空");
        }
        Application applicationConfig = applicationConfigService
                .selectApplicationConfigById(applicationConfigDto.getId());
        if (applicationConfig == null)
        {
            log.info("通过id:" + applicationConfigDto.getId() + "获取应用失败");
            throw new RuntimeException("获取应用失败");
        }
        applicationConfig.setUpdateTime(new Date());
        applicationConfig.setRegionId(applicationConfigDto.getRegionId());
        applicationConfig.setProfile(applicationConfigDto.getProfile());
        applicationConfig.setLabel(applicationConfigDto.getLabel());
        applicationConfig.setApplicationName(applicationConfigDto.getApplicationName());
        applicationConfig.setApplication(applicationConfigDto.getApplication());
        int a = applicationConfigService.update(applicationConfig);
        if (a > 0)
        {
            return true;
        }
        return false;
    }

    /**
     * [简要描述]:删除<br/>
     * [详细描述]:<br/>
     *
     * @param id :
     * @return java.lang.Boolean
     * jun.liu  2018/11/28 - 14:54
     **/
    @RequestMapping(value = "/delete/{id}")
    public Boolean delete(@PathVariable("id") Long id)
    {
        if (null == id)
        {
            log.info("删除应用失败，id不能为空");
            throw new RuntimeException("参数不能为空");
        }
        int a = applicationConfigService.delete(id);
        if (a > 0)
        {
            return true;
        }
        return false;
    }

    /**
     * [简要描述]:应用更新配置<br/>
     * [详细描述]:<br/>
     *
     * @param id : 应用主键ID
     * @return boolean
     * llxiao  2019/1/30 - 10:52
     **/
    @RequestMapping("/refresh")
    public boolean refresh(@RequestParam("id") Long id)
    {
        return applicationConfigService.refresh(id);
    }

    /**
     * [简要描述]:批量发布<br/>
     * [详细描述]:<br/>
     *
     * @param id : 客户端ID集合
     * @return boolean
     * llxiao  2019/3/27 - 20:05
     **/
    @RequestMapping("/batchRefresh")
    public boolean batchRefresh(@RequestBody Long[] id)
    {
        return this.applicationConfigService.batchRefresh(id);
    }

    /**
     * [简要描述]:获取已绑定该应用的配置组<br/>
     * [详细描述]:<br/>
     *
     * @param configItemGroupQuery :
     * @return com.github.pagehelper.PageInfo<com.winner.config.center.pojo.db.dto.ConfigItemGroupDto>
     * jun.liu  2018/11/28 - 14:59
     **/
    @RequestMapping(value = "/isRefApp")
    public PageInfo<ConfigItemGroupDto> pageRefGroupWithApp(@RequestBody ConfigItemGroupQuery configItemGroupQuery)
    {
        if (null == configItemGroupQuery.getAppId())
        {
            log.info("获取已绑定该应用的配置组失败,appId不能为空");
            throw new RuntimeException("参数不能为空");
        }
        return configItemGroupService
                .pageRefGroupWithApp(configItemGroupQuery, configItemGroupQuery.getPageNum(), configItemGroupQuery
                        .getPageSize());
    }

    /**
     * [简要描述]:获取未绑定该应用的配置组<br/>
     * [详细描述]:<br/>
     *
     * @param configItemGroupQuery :
     * @return com.github.pagehelper.PageInfo<com.winner.config.center.pojo.db.dto.ConfigItemGroupDto>
     * jun.liu  2018/11/28 - 15:31
     **/
    @RequestMapping(value = "/notRefApp")
    public PageInfo<ConfigItemGroupDto> pageNotRefGroupWithApp(@RequestBody ConfigItemGroupQuery configItemGroupQuery)
    {
        if (null == configItemGroupQuery.getAppId())
        {
            log.info("获取未绑定该应用的配置组失败,appId不能为空");
            throw new RuntimeException("参数不能为空");
        }
        return configItemGroupService
                .pageNotRefGroupWithApp(configItemGroupQuery, configItemGroupQuery.getPageNum(), configItemGroupQuery
                        .getPageSize());
    }

    /**
     * [简要描述]:批量绑定<br/>
     * [详细描述]:<br/>
     *
     * @param groupIds :
     * @param appId :
     * @return java.lang.Boolean
     * jun.liu  2018/11/28 - 15:34
     **/
    @RequestMapping(value = "/batchSaveRef")
    public Boolean batchSave(String groupIds, Long appId)
    {
        if (StringUtils.isBlank(groupIds) || null == appId)
        {
            log.info("绑定应用与配置组失败,{groupIds}、{appId}不能为空", groupIds, appId);
            throw new RuntimeException("参数不能为空");
        }
        Application applicationConfig = applicationConfigService.selectApplicationConfigById(appId);
        if (null == applicationConfig)
        {
            log.info("绑定应用与配置组失败,{appId}找不到对应的应用信息", appId);
            return false;
        }
        String[] groupIdArr = groupIds.split(",");
        int a = applicationItemGroupRelationService.batchSave(groupIdArr, appId);
        if (a > 0)
        {
            return true;
        }
        return false;
    }

    /**
     * [简要描述]:删除绑定<br/>
     * [详细描述]:<br/>
     *
     * @param groupIds :
     * @param appId :
     * @return java.lang.Boolean
     * jun.liu  2018/11/28 - 15:39
     **/
    @RequestMapping(value = "/batchDeleteRef")
    public Boolean batchDelete(String groupIds, Long appId)
    {
        if (StringUtils.isBlank(groupIds) || null == appId)
        {
            log.info("删除应用与配置组失败,{groupIds}、{appId}不能为空", groupIds, appId);
            throw new RuntimeException("参数不能为空");
        }
        String[] groupIdArr = groupIds.split(",");
        int a = applicationItemGroupRelationService.batchDelete(groupIdArr, appId);
        if (a > 0)
        {
            return true;
        }
        return false;
    }

    /**
     * [简要描述]:查收私有属性<br/>
     * [详细描述]:<br/>
     *
     * @param applicationConfigQuery
     * @return java.lang.Boolean
     * llxiao  2019/1/7 - 17:01
     **/
    @RequestMapping("/queryPrivateConfig")
    public PageInfo<ApplicationConfigDto> queryPrivateConfig(@RequestBody ApplicationConfigQuery applicationConfigQuery)
    {
        return this.applicationConfigService.pageQuery(applicationConfigQuery);
    }

    /**
     * [简要描述]:新增一条私有配置<br/>
     * [详细描述]:<br/>
     *
     * @param applicationConfigDto :
     * @return java.lang.Boolean
     * llxiao  2019/1/7 - 17:06
     **/
    @RequestMapping("/savePrivateConfig")
    public Boolean savePrivateConfig(@RequestBody ApplicationConfigDto applicationConfigDto)
    {
        ApplicationConfig config = convertPrivateConf(applicationConfigDto);
        return this.applicationConfigService.saveApplicationConfig(config);
    }

    /**
     * [简要描述]:更新一条私有配置信息<br/>
     * [详细描述]:<br/>
     *
     * @param applicationConfigDto :
     * @return java.lang.Boolean
     * llxiao  2019/1/7 - 17:06
     **/
    @RequestMapping("/updatePrivateConfig")
    public Boolean updatePrivateConfig(@RequestBody ApplicationConfigDto applicationConfigDto)
    {
        ApplicationConfig config = convertPrivateConf(applicationConfigDto);
        return this.applicationConfigService.updateApplicationConfig(config);
    }

    /**
     * [简要描述]:删除某项私有属性<br/>
     * [详细描述]:<br/>
     *
     * @param id : 私有属性主键ID
     * @return java.lang.Boolean
     * llxiao  2019/1/8 - 9:20
     **/
    @RequestMapping("/delPrivateConfig")
    public Boolean delPrivateConfig(@RequestParam("id") Long id)
    {
        boolean flag = false;
        if (null != id)
        {
            flag = this.applicationConfigService.delPrivateConfig(id);
        }
        return flag;
    }

    private ApplicationConfig convertPrivateConf(ApplicationConfigDto applicationConfigDto)
    {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setId(applicationConfigDto.getId());
        applicationConfig.setApplicationId(applicationConfigDto.getApplicationId());
        applicationConfig.setItemKey(applicationConfigDto.getItemKey());
        applicationConfig.setItemValue(applicationConfigDto.getItemValue());
        applicationConfig.setItemDesc(applicationConfigDto.getItemDesc());
        applicationConfig.setCreateTime(applicationConfigDto.getCreateTime());
        applicationConfig.setUpdateTime(applicationConfigDto.getUpdateTime());
        return applicationConfig;
    }
}
