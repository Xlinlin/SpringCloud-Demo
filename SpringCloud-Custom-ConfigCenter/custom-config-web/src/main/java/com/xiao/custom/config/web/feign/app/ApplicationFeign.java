package com.xiao.custom.config.web.feign.app;

import com.github.pagehelper.PageInfo;
import com.xiao.custom.config.pojo.dto.ApplicationConfigDto;
import com.xiao.custom.config.pojo.dto.ApplicationDto;
import com.xiao.custom.config.pojo.dto.ConfigItemGroupDto;
import com.xiao.custom.config.pojo.query.AppQuery;
import com.xiao.custom.config.pojo.query.ApplicationConfigQuery;
import com.xiao.custom.config.pojo.query.ConfigItemGroupQuery;
import com.xiao.custom.config.web.commo.Constants;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/12/20 10:20
 * @since JDK 1.8
 */
@FeignClient(value = Constants.CONFIG_SERVICE)
@RequestMapping("/application")
public interface ApplicationFeign
{
    /**
     * [简要描述]:分页获取<br/>
     * [详细描述]:<br/>
     *
     * @param appQuery :
     * @return com.github.pagehelper.PageInfo<com.winner.config.center.pojo.db.dto.ApplicationConfigDto>
     * jun.liu  2018/11/28 - 10:20
     **/
    @RequestMapping(value = "page")
    PageInfo<ApplicationDto> pageApplicationConfig(@RequestBody AppQuery appQuery);

    /**
     * [简要描述]:新增应用<br/>
     * [详细描述]:<br/>
     *
     * @param applicationConfigDto :
     * @return java.lang.Boolean
     * jun.liu  2018/11/28 - 10:29
     **/
    @RequestMapping(value = "/save")
    Boolean save(@RequestBody ApplicationDto applicationConfigDto);

    /**
     * [简要描述]:删除应用<br/>
     * [详细描述]:<br/>
     *
     * @param id :  主键ID
     * @return java.lang.Boolean
     * llxiao  2018/12/24 - 10:24
     **/
    @RequestMapping(value = "/delete/{id}")
    Boolean delete(@PathVariable("id") Long id);

    /**
     * [简要描述]:应用更新配置<br/>
     * [详细描述]:<br/>
     *
     * @param id : 应用主键ID
     * @return boolean
     * llxiao  2019/1/30 - 10:52
     **/
    @RequestMapping("/refresh")
    boolean refresh(@RequestParam("id") Long id);

    /**
     * [简要描述]:更新配置<br/>
     * [详细描述]:<br/>
     *
     * @param applicationConfigDto : 更新
     * @return java.lang.Boolean
     * llxiao  2018/12/24 - 19:22
     **/
    @RequestMapping(value = "/update")
    Boolean update(@RequestBody ApplicationDto applicationConfigDto);

    /**
     * [简要描述]:获取已绑定该应用的配置组<br/>
     * [详细描述]:<br/>
     *
     * @param configItemGroupQuery :
     * @return com.github.pagehelper.PageInfo<com.winner.config.center.pojo.db.dto.ConfigItemGroupDto>
     * jun.liu  2018/11/28 - 14:59
     **/
    @RequestMapping(value = "/isRefApp")
    PageInfo<ConfigItemGroupDto> pageRefGroupWithApp(@RequestBody ConfigItemGroupQuery configItemGroupQuery);

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
    Boolean batchUnBind(@RequestParam("groupIds") String groupIds, @RequestParam("appId") Long appId);

    /**
     * [简要描述]:获取未绑定该应用的配置组<br/>
     * [详细描述]:<br/>
     *
     * @param configItemGroupQuery :
     * @return com.github.pagehelper.PageInfo<com.winner.config.center.pojo.db.dto.ConfigItemGroupDto>
     * jun.liu  2018/11/28 - 15:31
     **/
    @RequestMapping(value = "/notRefApp")
    PageInfo<ConfigItemGroupDto> pageNotRefGroupWithApp(@RequestBody ConfigItemGroupQuery configItemGroupQuery);

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
    Boolean batchSave(@RequestParam("groupIds") String groupIds, @RequestParam("appId") Long appId);

    /**
     * [简要描述]:查收私有属性<br/>
     * [详细描述]:<br/>
     *
     * @param applicationConfigQuery
     * @return java.lang.Boolean
     * llxiao  2019/1/7 - 17:01
     **/
    @RequestMapping("/queryPrivateConfig")
    PageInfo<ApplicationConfigDto> queryPrivateConfig(ApplicationConfigQuery applicationConfigQuery);

    /**
     * [简要描述]:新增一条私有配置<br/>
     * [详细描述]:<br/>
     *
     * @param applicationConfigDto :
     * @return java.lang.Boolean
     * llxiao  2019/1/7 - 17:06
     **/
    @RequestMapping("/savePrivateConfig")
    Boolean savePrivateConfig(ApplicationConfigDto applicationConfigDto);

    /**
     * [简要描述]:更新一条私有配置信息<br/>
     * [详细描述]:<br/>
     *
     * @param applicationConfigDto :
     * @return java.lang.Boolean
     * llxiao  2019/1/7 - 17:06
     **/
    @RequestMapping("/updatePrivateConfig")
    Boolean updatePrivateConfig(ApplicationConfigDto applicationConfigDto);

    /**
     * [简要描述]:删除某项私有属性<br/>
     * [详细描述]:<br/>
     *
     * @param id : 私有属性主键ID
     * @return java.lang.Boolean
     * llxiao  2019/1/8 - 9:20
     **/
    @RequestMapping("/delPrivateConfig")
    Boolean delPrivateConfig(@RequestParam("id") Long id);

}
