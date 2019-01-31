package com.xiao.custom.config.web.controller;

import com.github.pagehelper.PageInfo;
import com.xiao.custom.config.pojo.dto.RegionDto;
import com.xiao.custom.config.pojo.query.RegionQuery;
import com.xiao.custom.config.web.feign.region.RegionFeign;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * [简要描述]: 区域Controller
 * [详细描述]:
 *
 * @author mjye
 * @version 1.0, 2018/12/20 15:31
 * @since JDK 1.8
 */
@RestController
@RequestMapping(value = "/region")
@Slf4j
public class RegionController
{
    @Autowired
    private RegionFeign regionFeign;

    /**
     * [简要描述]:分页查询区域信息
     * [详细描述]:<br/>
     *
     * @param regionQuery : 分页查询对象
     * @return com.github.pagehelper.PageInfo<com.winner.config.center.web.dto.ConfigItemDto>
     * mjye  2018/12/20 - 15:52
     **/
    @RequestMapping(value = "/queryRegion")
    public PageInfo<RegionDto> pageConfigItem(@RequestBody RegionQuery regionQuery)
    {
        return regionFeign.queryRegion(regionQuery);
    }

    /**
     * [简要描述]:删除区域
     * [详细描述]:<br/>
     *
     * @return java.lang.Boolean
     * mjye  2018/12/20 - 17:28
     **/
    @RequestMapping(value = "/delectRegion/{ids}")
    public Boolean delectRegion(@PathVariable("ids") String ids)
    {
        if (null == ids)
        {
            log.info("删除区域信息失败，id不能为空");
            throw new RuntimeException("参数不能为空");
        }
        return regionFeign.delete(ids);
    }

    //    /**
    //     * [简要描述]:更新区域信息
    //     * [详细描述]:<br/>
    //      * @param regionDto :
    //     * @return java.lang.Boolean
    //     * mjye  2018/12/20 - 17:29
    //     **/
    //    @PostMapping(value="/updateRegion")
    //    public  Boolean updateRegion(@RequestBody RegionDto regionDto){
    //        if(null == regionDto.getId()  ||  StringUtils.isBlank(regionDto.getRegionDesc()) || StringUtils.isBlank(regionDto.getRegionName())
    //                || null == regionDto.getCreateTime()) {
    //            log.info("更改区域信息失败,参数不能为空");
    //            throw new RuntimeException("参数不能为空");
    //        }
    //        return  regionFeign.updateRegion(regionDto);
    //    }

    /**
     * [简要描述]:新增or修改区域信息
     * [详细描述]:<br/>
     *
     * @param regionDto :
     * @return java.lang.Boolean
     * mjye  2018/12/20 - 17:31
     **/
    @PostMapping(value = "/addRegion")
    public Boolean addRegion(@RequestBody RegionDto regionDto)
    {
        if (null == regionDto.getId())
        {
            if (StringUtils.isBlank(regionDto.getRegionDesc()) || StringUtils.isBlank(regionDto.getRegionName()))
            {
                log.info("新增区域信息失败,参数不能为空");
                throw new RuntimeException("参数不能为空");
            }
            return regionFeign.addRegion(regionDto);
        }
        else
        {
            if (StringUtils.isBlank(regionDto.getRegionDesc()) || StringUtils.isBlank(regionDto.getRegionName()))
            {
                log.info("更改区域信息失败,参数不能为空");
                throw new RuntimeException("参数不能为空");
            }
            return regionFeign.updateRegion(regionDto);
        }
    }
}