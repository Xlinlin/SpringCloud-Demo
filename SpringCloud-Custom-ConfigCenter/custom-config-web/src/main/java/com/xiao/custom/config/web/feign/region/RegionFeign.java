package com.xiao.custom.config.web.feign.region;

import com.github.pagehelper.PageInfo;
import com.xiao.custom.config.pojo.dto.RegionDto;
import com.xiao.custom.config.pojo.query.RegionQuery;
import com.xiao.custom.config.web.commo.Constants;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author mjye
 * @version 1.0, 2018/12/20 15:39
 * @since JDK 1.8
 */
@FeignClient(value = Constants.CONFIG_SERVICE)
@RequestMapping(value = "/region")
public interface RegionFeign
{
    //分页查询区域
    @RequestMapping(value = "/queryRegion")
    PageInfo<RegionDto> queryRegion(@RequestBody RegionQuery regionQuery);

    //删除区域
    @PostMapping(value="/delectRegion/{ids}")
    Boolean delete(@PathVariable("ids") String ids);

    //更新区域信息
    @PostMapping(value="/updateRegion")
    Boolean updateRegion(@RequestBody RegionDto regionDto);

    //新增区域信息
    @PostMapping(value="/addRegion")
    Boolean addRegion(@RequestBody RegionDto regionDto);

    //查询所有的 Region
    @RequestMapping(value = "/selectRgion")
    List<RegionDto> selectRgion();
}
