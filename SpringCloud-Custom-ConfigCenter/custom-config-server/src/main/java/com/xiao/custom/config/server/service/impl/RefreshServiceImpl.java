package com.xiao.custom.config.server.service.impl;

import com.xiao.custom.config.server.netty.dto.CommandEnum;
import com.xiao.custom.config.server.netty.dto.Message;
import com.xiao.custom.config.server.netty.manager.Connection;
import com.xiao.custom.config.server.netty.manager.ConnectionManager;
import com.xiao.custom.config.server.service.RefreshService;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/4/4 09:45
 * @since JDK 1.8
 */
@Service
@Slf4j
public class RefreshServiceImpl implements RefreshService
{
    @Resource
    private ConnectionManager connectionManager;

    /**
     * [简要描述]:刷新服务<br/>
     * [详细描述]:<br/>
     *
     * @param ip :
     * @param port :
     * @return void
     * llxiao  2019/4/4 - 9:44
     **/
    @Override
    public boolean refresh(String ip, int port)
    {
        boolean flag = false;
        if (StringUtils.isNotBlank(ip))
        {
            Connection connection = connectionManager.getConnection(ip, port);
            if (null != connection)
            {
                log.info(">>> 服务端通知IP:{},PORT:{}的客户端刷新配置....", connection.getServerIp(), connection.getServerPort());
                Channel channel = connection.getChannel();
                Message message = new Message();
                message.setCommand(CommandEnum.REFRESH.getStatus());
                message.setMessage("服务通知客户端发起刷新配置");
                channel.writeAndFlush(message);
                flag = true;
            }
            else
            {
                log.error(">>> 无法发起刷新配置请求，当前IP:{}和PORT:{}找不到对应的客户端信息...", ip, port);
            }
        }
        return flag;
    }
}
