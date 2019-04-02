package com.xiao.custom.config.service.api;

import com.github.pagehelper.PageInfo;
import com.xiao.custom.config.pojo.dto.ClientHostInfoDto;
import com.xiao.custom.config.pojo.query.ClientHostInfoQuery;
import com.xiao.custom.config.service.service.ClientHostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/3/27 15:36
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/clientInfo")
@Slf4j
public class ClientHostApi
{
    @Autowired
    private ClientHostService clientHostService;

    /**
     * 分页查询客户端信息
     *
     * @param query
     * @return
     */
    @RequestMapping("/page")
    public PageInfo<ClientHostInfoDto> pageQuery(@RequestBody ClientHostInfoQuery query)
    {
        return clientHostService.pageQuery(query);
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
    public boolean deleteById(@RequestParam("id") Long id)
    {
        if (null != id)
        {
            clientHostService.delete(id);
        }
        else
        {
            return false;
        }

        return true;
    }
}
