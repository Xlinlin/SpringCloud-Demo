package com.xiao.custom.config.pojo.mapper;

import com.xiao.custom.config.pojo.entity.ClientApplication;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Mybatis Generator on 2019/01/29
 */
public interface ClientApplicationMapper
{
    int deleteByPrimaryKey(Long id);

    int insert(ClientApplication record);

    int insertSelective(ClientApplication record);

    ClientApplication selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ClientApplication record);

    int updateByPrimaryKey(ClientApplication record);

    /**
     * [简要描述]:修改应用状态<br/>
     * [详细描述]:<br/>
     *
     * @param application :
     * @param profile :
     * @param status :
     * @return void
     * llxiao  2019/1/30 - 16:11
     **/
    void updateStatus(@Param("application") String application, @Param("profile") String profile,
            @Param("status") int status);
}