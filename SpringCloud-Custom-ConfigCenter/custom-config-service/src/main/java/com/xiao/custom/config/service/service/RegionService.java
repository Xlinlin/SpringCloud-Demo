package com.xiao.custom.config.service.service;

import com.github.pagehelper.PageInfo;
import com.xiao.custom.config.pojo.dto.RegionDto;
import com.xiao.custom.config.pojo.entity.Region;
import com.xiao.custom.config.pojo.query.RegionQuery;

import java.util.List;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author jun.liu
 * @version 1.0, 2018/11/26 17:22
 * @since JDK 1.8
 */
public interface RegionService
{
    int save(RegionDto regionDto);

    int update(RegionDto regionDto);

    int delete(Long id);
    Region selectRegionById(Long id);

    PageInfo<RegionDto> pageRegion(RegionQuery regionQuery, Integer pageNum, Integer pageSize);

    List<RegionDto> selectRegion();

    int batchDelete(String[] idArr);
}
