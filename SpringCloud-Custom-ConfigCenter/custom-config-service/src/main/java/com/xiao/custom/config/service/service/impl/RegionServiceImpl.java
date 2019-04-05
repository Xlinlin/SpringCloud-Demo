package com.xiao.custom.config.service.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiao.custom.config.pojo.dto.RegionDto;
import com.xiao.custom.config.pojo.entity.Region;
import com.xiao.custom.config.pojo.mapper.RegionMapper;
import com.xiao.custom.config.pojo.query.RegionQuery;
import com.xiao.custom.config.service.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author jun.liu
 * @version 1.0, 2018/11/26 17:22
 * @since JDK 1.8
 */
@Service
public class RegionServiceImpl implements RegionService
{
    @Autowired
    private RegionMapper regionMapper;

    /**
     * [简要描述]:添加区域信息<br/>
     * [详细描述]:<br/>
     *
     * @return Integer
     **/
    @Override
    @Transactional
    public int save(RegionDto regionDto)
    {
        regionDto.setCreateTime(new Date());
        Region region = regionDtoconvertRegion(regionDto);

        return regionMapper.insert(region);
    }

    /**
     * [简要描述]:更新区域信息<br/>
     * [详细描述]:<br/>
     *
     * @return Integer
     **/
    @Override
    @Transactional
    public int update(RegionDto regionDto)
    {
        Region region = regionDtoconvertRegion(regionDto);
        return regionMapper.updateByPrimaryKey(region);
    }

    /**
     * [简要描述]:根据id删除<br/>
     * [详细描述]:<br/>
     *
     * @param id:
     * @return int
     **/
    @Override
    @Transactional
    public int delete(Long id)
    {
        return regionMapper.deleteByPrimaryKey(id);
    }

    /**
     * [简要描述]:分页查询<br/>
     * [详细描述]:<br/>
     *
     * @return RegionDto
     **/
    @Override
    public PageInfo<RegionDto> pageRegion(RegionQuery regionQuery, Integer pageNum, Integer pageSize)
    {
        PageHelper.startPage(pageNum, pageSize);
        List<RegionDto> list = regionMapper.pageRegion(regionQuery);
        return new PageInfo<>(list);
    }

    /**
     * [简要描述]:查询所有的region
     * [详细描述]:<br/>
     *
     * @return java.util.List<com.winner.config.center.pojo.db.dto.RegionDto>
     * mjye  2018/12/21 - 16:58
     **/
    @Override
    public List<RegionDto> selectRegion()
    {
        return regionMapper.selectRegion();
    }

    /**
     * [简要描述]:批量删除
     * [详细描述]:<br/>
     *
     * @param idArr :
     * @return int
     * mjye  2018/12/25 - 11:09
     **/
    @Override
    public int batchDelete(String[] idArr)
    {
        return regionMapper.batchDelete(idArr);
    }

    /**
     * [简要描述]:RegionDto转Region<br/>
     * [详细描述]:RegionDto转Region<br/>
     *
     * @return Region
     **/
    public Region regionDtoconvertRegion(RegionDto regionDto)
    {
        Region region = new Region();
        region.setId(regionDto.getId());
        region.setCreateTime(regionDto.getCreateTime());
        region.setRegionDesc(regionDto.getRegionDesc());
        region.setRegionName(regionDto.getRegionName());
        region.setUpdateTime(new Date());

        return region;
    }

    /**
     * [简要描述]:根据id查询区域
     * [详细描述]:<br/>
     *
     * @param id :
     * @return com.winner.config.center.pojo.db.entity.Region
     * mjye  2018/12/21 - 16:57
     **/
    @Override
    public Region selectRegionById(Long id)
    {
        return regionMapper.selectByPrimaryKey(id);
    }

}
