package com.xiao.custom.config.web.controller.app;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.PageInfo;
import com.xiao.custom.config.pojo.dto.ApplicationConfigDto;
import com.xiao.custom.config.pojo.dto.ApplicationDto;
import com.xiao.custom.config.pojo.dto.ConfigItemGroupDto;
import com.xiao.custom.config.pojo.dto.RegionDto;
import com.xiao.custom.config.pojo.query.AppQuery;
import com.xiao.custom.config.pojo.query.ApplicationConfigQuery;
import com.xiao.custom.config.pojo.query.ConfigItemGroupQuery;
import com.xiao.custom.config.web.controller.app.vo.ApplicationVo;
import com.xiao.custom.config.web.controller.app.vo.RegionVo;
import com.xiao.custom.config.web.feign.app.ApplicationFeign;
import com.xiao.custom.config.web.feign.region.RegionFeign;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/12/20 10:19
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/appManager")
@Slf4j
public class AppManagerController
{
    @Autowired
    private ApplicationFeign applicationFeign;

    @Autowired
    private RegionFeign regionFeign;

    @PostMapping("/pageQuery")
    public PageInfo<ApplicationDto> pageQuery(@RequestBody AppQuery appQuery)
    {
        return applicationFeign.pageApplicationConfig(appQuery);
    }

    @RequestMapping("/queryAllRegion")
    public List<RegionVo> queryAllRegion()
    {
        List<RegionVo> regionVos;
        List<RegionDto> regionDtos = regionFeign.selectRgion();
        if (CollectionUtil.isNotEmpty(regionDtos))
        {
            regionVos = new ArrayList<>(regionDtos.size());
            for (RegionDto regionDto : regionDtos)
            {
                regionVos.add(convertVo(regionDto));
            }
        }
        else
        {
            regionVos = null;
        }
        return regionVos;
    }

    @RequestMapping("/save")
    public boolean saveApplication(@RequestBody ApplicationVo applicationVo)
    {
        return applicationFeign.save(convertDto(applicationVo));
    }

    @RequestMapping("/update")
    public Boolean update(@RequestBody ApplicationVo applicationVo)
    {
        if (applicationVo.getId() == null)
        {
            return false;
        }
        else
        {
            return applicationFeign.update(convertDto(applicationVo));
        }
    }

    //删除应用，以及包括他所属下管理的所有配置
    @RequestMapping("/delete")
    public boolean deleteApplication(Long id)
    {
        if (null != id)
        {
            return applicationFeign.delete(id);
        }
        return false;
    }

    /**
     * [简要描述]:发布配置<br/>
     * [详细描述]:<br/>
     *
     * @param id :
     * @return boolean
     * llxiao  2019/1/30 - 10:50
     **/
    @RequestMapping("/refresh")
    public boolean refresh(Long id)
    {
        if (null != id)
        {
            return applicationFeign.refresh(id);
        }
        return false;
    }

    @RequestMapping("/queryItemGroup")
    public PageInfo<ConfigItemGroupDto> pageRefGroupWithApp(@RequestBody ConfigItemGroupQuery configItemGroupQuery)
    {
        if (null == configItemGroupQuery.getAppId())
        {
            log.error("应用ID不能为空!");
            new PageInfo<>();
        }
        return this.applicationFeign.pageRefGroupWithApp(configItemGroupQuery);
    }

    /**
     * [简要描述]:应用ID和组ID删除对应关系<br/>
     * [详细描述]:<br/>
     *
     * @param appId : 应用ID
     * @param itemGroupId : 组ID
     * @return boolean
     * llxiao  2018/12/25 - 17:38
     **/
    @RequestMapping("delItemGroup")
    public boolean delItemGroup(Long appId, Long itemGroupId)
    {
        if (null != appId && null != itemGroupId)
        {
            return applicationFeign.batchUnBind(String.valueOf(itemGroupId), appId);
        }
        return false;
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
        if (configItemGroupQuery.getAppId() == null)
        {
            return new PageInfo<>();
        }
        return this.applicationFeign.pageNotRefGroupWithApp(configItemGroupQuery);
    }

    /**
     * [简要描述]:绑定应用<br/>
     * [详细描述]:<br/>
     *
     * @param groupIds :
     * @param appId :
     * @return java.lang.Boolean
     * llxiao  2019/1/2 - 14:48
     **/
    @RequestMapping(value = "/batchSaveRef")
    public Boolean batchSave(@RequestParam("groupIds") String groupIds, @RequestParam("appId") Long appId)
    {
        if (StringUtils.isEmpty(groupIds) || null == appId)
        {
            return false;
        }
        return this.applicationFeign.batchSave(groupIds, appId);
    }

    /**
     * [简要描述]:应用查询关联的私有配置属性<br/>
     * [详细描述]:<br/>
     *
     * @param applicationConfigQuery :
     * @return com.github.pagehelper.PageInfo<com.winner.config.center.pojo.db.dto.ApplicationConfigDto>
     * llxiao  2019/1/7 - 15:24
     **/
    @RequestMapping("/queryPrivateConfig")
    public PageInfo<ApplicationConfigDto> pageQuery(@RequestBody ApplicationConfigQuery applicationConfigQuery)
    {
        if (null == applicationConfigQuery.getApplicationId())
        {
            return new PageInfo<>();
        }
        return this.applicationFeign.queryPrivateConfig(applicationConfigQuery);
    }

    //新增私有配置项
    @RequestMapping("/addPrivateItem")
    public Boolean savePrivateConfig(@RequestBody ApplicationConfigDto applicationConfigDto)
    {
        if (null == applicationConfigDto.getApplicationId())
        {
            return false;
        }
        return this.applicationFeign.savePrivateConfig(applicationConfigDto);
    }

    //修改私有配置项
    @RequestMapping("/updatePrivateItem")
    public Boolean updatePrivateConfig(@RequestBody ApplicationConfigDto applicationConfigDto)
    {
        if (null == applicationConfigDto.getId())
        {
            return false;
        }
        return this.applicationFeign.updatePrivateConfig(applicationConfigDto);
    }

    //删除私有配置项
    @RequestMapping("/delPrivateItem")
    public Boolean delPrivateConfig(Long id)
    {
        if (null == id)
        {
            return false;
        }
        return this.applicationFeign.delPrivateConfig(id);
    }

    private ApplicationDto convertDto(ApplicationVo applicationVo)
    {
        ApplicationDto applicationConfigDto = new ApplicationDto();
        applicationConfigDto.setId(applicationVo.getId());
        applicationConfigDto.setApplication(applicationVo.getApplication());
        applicationConfigDto.setApplicationName(applicationVo.getApplicationName());
        applicationConfigDto.setLabel(applicationVo.getLabel());
        applicationConfigDto.setProfile(applicationVo.getProfile());
        applicationConfigDto.setCreateTime(new Date());
        applicationConfigDto.setUpdateTime(new Date());
        applicationConfigDto.setRegionId(applicationVo.getRegion());
        applicationConfigDto.setGroupIds(applicationVo.getConfGroupIds());
        return applicationConfigDto;
    }

    private RegionVo convertVo(RegionDto regionDto)
    {
        RegionVo regionVo = new RegionVo();
        regionVo.setLabel(regionDto.getRegionName());
        regionVo.setValue(regionDto.getId());
        return regionVo;
    }
}
