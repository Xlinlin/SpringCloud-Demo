package com.xiao.custom.config.pojo.mapper;

import com.xiao.custom.config.pojo.dto.ClientHostInfoDto;
import com.xiao.custom.config.pojo.entity.ClientHostInfo;
import com.xiao.custom.config.pojo.query.ClientHostInfoQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Mybatis Generator on 2019/01/29
 */
public interface ClientHostInfoMapper
{
    int deleteByPrimaryKey(Long id);

    int insert(ClientHostInfo record);

    int insertSelective(ClientHostInfo record);

    ClientHostInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ClientHostInfo record);

    int updateByPrimaryKey(ClientHostInfo record);

    /**
     * [简要描述]:应用名称+环境查询已连接的应用信息<br/>
     * [详细描述]:<br/>
     *
     * @param application :
     * @param profile :
     * @return java.util.List<com.winner.config.center.pojo.db.entity.ClientHostInfo>
     * llxiao  2019/1/30 - 11:15
     **/
    List<ClientHostInfo> queryByApplication(@Param("application") String application, @Param("profile") String profile);

    /**
     * [简要描述]:修改服务状态<br/>
     * [详细描述]:0在线，1下线<br/>
     *
     * @param id :
     * @param status:
     * @return int
     * llxiao  2019/1/30 - 14:18
     **/
    int updateStatus(@Param("id") Long id, @Param("status") int status);

    /**
     * [简要描述]:分页查询客户端连接信息<br/>
     * [详细描述]:<br/>
     *
     * @param query : 查询条件
     * @return java.util.List<com.winner.config.center.pojo.db.dto.ClientHostInfoDto>
     * llxiao  2019/3/27 - 14:46
     **/
    List<ClientHostInfoDto> pageQuery(ClientHostInfoQuery query);
}