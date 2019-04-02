package com.xiao.custom.config.web.controller;

import com.github.pagehelper.PageInfo;
import com.xiao.custom.config.pojo.dto.ClientHostInfoDto;
import com.xiao.custom.config.pojo.query.ClientHostInfoQuery;
import com.xiao.custom.config.web.feign.client.ClientInfoFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * [简要描述]: 客户端服务
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/3/27 15:55
 * @since JDK 1.8
 */
@RestController
@RequestMapping(value = "/api/config/clientInfo")
@Slf4j
public class ClientInfoController
{
    @Autowired
    private ClientInfoFeign clientInfoFeign;

    /**
     * 分页查询客户端信息
     *
     * @param query
     * @return
     */
    @RequestMapping("/page")
    public PageInfo<ClientHostInfoDto> pageQuery(@RequestBody ClientHostInfoQuery query)
    {
        return clientInfoFeign.pageQuery(query);
    }

    /**
     * [简要描述]:删除数据<br/>
     * [详细描述]:<br/>
     *
     * @param id :
     * @return boolean
     * llxiao  2019/3/27 - 15:46
     **/
    @RequestMapping("/del")
    public boolean deleteById(Long id)
    {
        if (null != id)
        {
            return clientInfoFeign.deleteById(id);
        }
        else
        {
            log.error("删除失败，客户端注解ID为空!");
        }
        return false;
    }
}
