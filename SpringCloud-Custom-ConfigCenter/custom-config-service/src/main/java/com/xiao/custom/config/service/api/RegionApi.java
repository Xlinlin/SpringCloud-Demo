package com.xiao.custom.config.service.api;

import com.github.pagehelper.PageInfo;
import com.xiao.custom.config.pojo.dto.RegionDto;
import com.xiao.custom.config.pojo.entity.Region;
import com.xiao.custom.config.pojo.query.RegionQuery;
import com.xiao.custom.config.service.service.RegionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * [简要描述]:区域管理
 * [详细描述]:区域管理
 *
 * @author jyqiao
 * @version 1.0, 2018年11月27日 上午11:29:12
 * @since JDK 1.8
 */
@RestController
@RequestMapping(value = "/region")
@Slf4j
public class RegionApi
{
    @Autowired
    private RegionService regionService;

    //分页查询区域
    @RequestMapping(value = "/queryRegion")
    public PageInfo<RegionDto> queryRegion(@RequestBody RegionQuery regionQuery)
    {
        return regionService.pageRegion(regionQuery, regionQuery.getPageNum(), regionQuery.getPageSize());
    }

    /**
     * [简要描述]:查询所有的region
     * [详细描述]:<br/>
     *
     * @return java.util.List<com.winner.config.center.pojo.db.dto.RegionDto>
     * mjye  2018/12/21 - 17:00
     **/
    @RequestMapping(value = "/selectRgion")
    public List<RegionDto> selectRgion()
    {
        return regionService.selectRegion();
    }

    //删除区域
    @PostMapping(value = "/delectRegion/{ids}")
    public Boolean delectRegion(@PathVariable("ids") String ids)
    {
        if (null == ids)
        {
            log.info("删除区域信息失败，id不能为空");
            throw new RuntimeException("参数不能为空");
        }
        String[] idArr = ids.split(",");
        int a = regionService.batchDelete(idArr);
        if (a > 0)
        {
            return true;
        }
        return false;
    }

    //更新区域信息
    @PostMapping(value = "/updateRegion")
    public Boolean updateRegion(@RequestBody RegionDto regionDto)
    {
        if (null == regionDto.getId() || StringUtils.isBlank(regionDto.getRegionDesc()) || StringUtils
                .isBlank(regionDto.getRegionName()) || null == regionDto.getCreateTime())
        {
            log.info("更改区域信息失败,参数不能为空");
            throw new RuntimeException("参数不能为空");

        }
        int b = regionService.update(regionDto);

        if (b > 0)
        {
            return true;
        }
        return false;

    }

    //新增区域信息
    @PostMapping(value = "/addRegion")
    public Boolean addRegion(@RequestBody RegionDto regionDto)
    {
        if (StringUtils.isBlank(regionDto.getRegionDesc()) || StringUtils.isBlank(regionDto.getRegionName()))
        {
            log.info("新增区域信息失败,参数不能为空");
            throw new RuntimeException("参数不能为空");

        }
        int b = regionService.save(regionDto);
        if (b > 0)
        {
            return true;
        }
        return false;

    }
    //根据id查询区域信息

    @RequestMapping(value = "selectRegionById/{id}")
    public RegionDto selectRegionById(@PathVariable Long id)
    {
        if (null == id)
        {
            log.info("获取区域信息失败，id不能为空");
            throw new RuntimeException("参数不能为空");
        }

        return RegionToDto(regionService.selectRegionById(id));
    }

    //po转dto
    public RegionDto RegionToDto(Region region)
    {
        RegionDto regionDto = new RegionDto();
        regionDto.setId(region.getId());
        regionDto.setCreateTime(region.getCreateTime());
        regionDto.setRegionDesc(region.getRegionDesc());
        regionDto.setRegionName(region.getRegionName());
        regionDto.setUpdateTime(region.getUpdateTime());

        return regionDto;
    }

}
