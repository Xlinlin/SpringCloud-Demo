/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xiao.custom.config.client.netty.util;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.*;
import java.util.Enumeration;

/**
 * Some utilities for remoting.
 *
 * @author jiangping
 * @version $Id: RemotingUtil.java, v 0.1 Mar 30, 2016 11:51:02 AM jiangping Exp $
 */
@Slf4j
public class RemotingUtil
{

    /**
     * Parse the remote address of the channel.
     *
     * @param channel
     * @return
     */
    public static String parseRemoteAddress(final Channel channel)
    {
        if (null == channel)
        {
            return StringUtils.EMPTY;
        }
        final SocketAddress remote = channel.remoteAddress();
        return doParse(remote != null ? remote.toString().trim() : StringUtils.EMPTY);
    }

    /**
     * Parse the local address of the channel.
     *
     * @param channel
     * @return
     */
    public static String parseLocalAddress(final Channel channel)
    {
        if (null == channel)
        {
            return StringUtils.EMPTY;
        }
        final SocketAddress local = channel.localAddress();
        return doParse(local != null ? local.toString().trim() : StringUtils.EMPTY);
    }

    /**
     * Parse the remote host ip of the channel.
     *
     * @param channel
     * @return
     */
    public static String parseRemoteIP(final Channel channel)
    {
        if (null == channel)
        {
            return StringUtils.EMPTY;
        }
        final InetSocketAddress remote = (InetSocketAddress) channel.remoteAddress();
        if (remote != null)
        {
            return remote.getAddress().getHostAddress();
        }
        return StringUtils.EMPTY;
    }

    /**
     * Parse the remote hostname of the channel.
     * <p>
     * Note: take care to use this method, for a reverse name lookup takes uncertain time in {@link InetAddress#getHostName}.
     *
     * @param channel
     * @return
     */
    public static String parseRemoteHostName(final Channel channel)
    {
        if (null == channel)
        {
            return StringUtils.EMPTY;
        }
        final InetSocketAddress remote = (InetSocketAddress) channel.remoteAddress();
        if (remote != null)
        {
            return remote.getAddress().getHostName();
        }
        return StringUtils.EMPTY;
    }

    /**
     * Parse the local host ip of the channel.
     *
     * @param channel
     * @return
     */
    public static String parseLocalIP(final Channel channel)
    {
        if (null == channel)
        {
            return StringUtils.EMPTY;
        }
        final InetSocketAddress local = (InetSocketAddress) channel.localAddress();
        if (local != null)
        {
            return local.getAddress().getHostAddress();
        }
        return StringUtils.EMPTY;
    }

    /**
     * Parse the remote host port of the channel.
     *
     * @param channel
     * @return int
     */
    public static int parseRemotePort(final Channel channel)
    {
        if (null == channel)
        {
            return -1;
        }
        final InetSocketAddress remote = (InetSocketAddress) channel.remoteAddress();
        if (remote != null)
        {
            return remote.getPort();
        }
        return -1;
    }

    /**
     * Parse the local host port of the channel.
     *
     * @param channel
     * @return int
     */
    public static int parseLocalPort(final Channel channel)
    {
        if (null == channel)
        {
            return -1;
        }
        final InetSocketAddress local = (InetSocketAddress) channel.localAddress();
        if (local != null)
        {
            return local.getPort();
        }
        return -1;
    }

    /**
     * Parse the socket address, omit the leading "/" if present.
     * <p>
     * e.g.1 /127.0.0.1:1234 -> 127.0.0.1:1234
     * e.g.2 sofatest-2.stack.alipay.net/10.209.155.54:12200 -> 10.209.155.54:12200
     *
     * @param socketAddress
     * @return String
     */
    public static String parseSocketAddressToString(SocketAddress socketAddress)
    {
        if (socketAddress != null)
        {
            return doParse(socketAddress.toString().trim());
        }
        return StringUtils.EMPTY;
    }

    /**
     * Parse the host ip of socket address.
     * <p>
     * e.g. /127.0.0.1:1234 -> 127.0.0.1
     *
     * @param socketAddress
     * @return String
     */
    public static String parseSocketAddressToHostIp(SocketAddress socketAddress)
    {
        final InetSocketAddress addrs = (InetSocketAddress) socketAddress;
        if (addrs != null)
        {
            InetAddress addr = addrs.getAddress();
            if (null != addr)
            {
                return addr.getHostAddress();
            }
        }
        return StringUtils.EMPTY;
    }

    /**
     * URL地址获取host信息
     *
     * @param url
     * @return
     */
    public static String getHost(String url)
    {
        String host = "";
        if (StringUtils.isNotBlank(url))
        {
            try
            {
                URL u = new URL(url);
                host = u.getHost();
            }
            catch (Exception e)
            {
                log.error("Url错误，获取不到主机信息!");
            }
        }

        return host;
    }

    /**
     * [简要描述]:获取本地IP地址<br/>
     * [详细描述]:<br/>
     *
     * @return java.lang.String
     * llxiao  2019/4/3 - 16:52
     **/
    public static String getLocalHost()
    {
        InetAddress inetAddress = getLocalHostLANAddress();
        if (null != inetAddress)
        {
            return inetAddress.getHostName();
        }
        else
        {
            return "";
        }
    }

    /**
     * [简要描述]:获取本地的IP地址<br/>
     * [详细描述]:<br/>
     *
     * @return java.net.InetAddress
     * llxiao  2019/4/3 - 16:44
     **/
    public static InetAddress getLocalHostLANAddress()
    {
        InetAddress jdkSuppliedAddress = null;
        try
        {
            InetAddress candidateAddress = null;
            InetAddress inetAddr;
            NetworkInterface iface;
            // 遍历所有的网络接口
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); )
            {
                iface = (NetworkInterface) ifaces.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); )
                {
                    inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress())
                    {// 排除loopback类型地址
                        if (inetAddr.isSiteLocalAddress())
                        {
                            // 如果是site-local地址，就是它了
                            return inetAddr;
                        }
                        else if (candidateAddress == null)
                        {
                            // site-local类型的地址未被发现，先记录候选地址
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null)
            {
                jdkSuppliedAddress = candidateAddress;
            }
            else
            {
                // 如果没有发现 non-loopback地址.只能用最次选的方案
                jdkSuppliedAddress = InetAddress.getLocalHost();
            }
        }
        catch (Exception e)
        {
            log.error("获取本地IP地址错误，错误信息:", e);
        }
        return jdkSuppliedAddress;
    }

    /**
     * <ol>
     * <li>if an address starts with a '/', skip it.
     * <li>if an address contains a '/', substring it.
     * </ol>
     *
     * @param addr
     * @return
     */
    private static String doParse(String addr)
    {
        if (StringUtils.isBlank(addr))
        {
            return StringUtils.EMPTY;
        }
        if (addr.charAt(0) == '/')
        {
            return addr.substring(1);
        }
        else
        {
            int len = addr.length();
            for (int i = 1; i < len; ++i)
            {
                if (addr.charAt(i) == '/')
                {
                    return addr.substring(i + 1);
                }
            }
            return addr;
        }
    }
}
