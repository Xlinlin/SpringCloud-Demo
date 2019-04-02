package com.xiao.custom.config.web.feign.client;

import com.github.pagehelper.PageInfo;
import com.xiao.custom.config.pojo.dto.ClientHostInfoDto;
import com.xiao.custom.config.pojo.query.ClientHostInfoQuery;
import com.xiao.custom.config.web.commo.Constants;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/3/27 15:52
 * @since JDK 1.8
 */
@FeignClient(value = Constants.CONFIG_SERVICE)
@RequestMapping("/clientInfo")
public interface ClientInfoFeign
{
    /**
     * 分页查询客户端信息
     *
     * @param query
     * @return
     */
    @RequestMapping("/page")
    PageInfo<ClientHostInfoDto> pageQuery(@RequestBody ClientHostInfoQuery query);

    /**
     * [简要描述]:删除数据<br/>
     * [详细描述]:<br/>
     *
     * @param id :
     * @return boolean
     * llxiao  2019/3/27 - 15:46
     **/
    @RequestMapping("/del")
    boolean deleteById(@RequestParam("id") Long id);

}
