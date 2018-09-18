#原文链接：https://blog.csdn.net/neosmith/article/details/53126924

问题场景
服务器上分别配置了eth0, eth1和eth2三块网卡，只有eth1的地址可供其它机器访问，eth0和eth2的 IP 无效。在这种情况下，服务注册时Eureka Client会自动选择eth0作为服务ip, 导致其它服务无法调用。

问题原因
由于官方并没有写明Eureka Client探测本机IP的逻辑，所以只能翻阅源代码。Eureka Client的源码在eureka-client模块下，com.netflix.appinfo包下的InstanceInfo类封装了本机信息，其中就包括了IP地址。在 Spring Cloud 环境下，Eureka Client并没有自己实现探测本机IP的逻辑，而是交给Spring的InetUtils工具类的findFirstNonLoopbackAddress()方法完成的：

public InetAddress findFirstNonLoopbackAddress() {
        InetAddress result = null;
        try {
            // 记录网卡最小索引
            int lowest = Integer.MAX_VALUE;
            // 获取所有网卡
            for (Enumeration<NetworkInterface> nics = NetworkInterface
                    .getNetworkInterfaces(); nics.hasMoreElements();) {
                NetworkInterface ifc = nics.nextElement();
                if (ifc.isUp()) {
                    log.trace("Testing interface: " + ifc.getDisplayName());
                    if (ifc.getIndex() < lowest || result == null) {
                        lowest = ifc.getIndex(); // 记录索引
                    }
                    else if (result != null) {
                        continue;
                    }

                    // @formatter:off
                    if (!ignoreInterface(ifc.getDisplayName())) { // 是否是被忽略的网卡
                        for (Enumeration<InetAddress> addrs = ifc
                                .getInetAddresses(); addrs.hasMoreElements();) {
                            InetAddress address = addrs.nextElement();
                            if (address instanceof Inet4Address
                                    && !address.isLoopbackAddress() 
                                    && !ignoreAddress(address)) { 
                                log.trace("Found non-loopback interface: "
                                        + ifc.getDisplayName());
                                result = address;
                            }
                        }
                    }
                    // @formatter:on
                }
            }
        }
        catch (IOException ex) {
            log.error("Cannot get first non-loopback address", ex);
        }

        if (result != null) {
            return result;
        }

        try {
            return InetAddress.getLocalHost(); // 如果以上逻辑都没有找到合适的网卡，则使用JDK的InetAddress.getLocalhost()
        }
        catch (UnknownHostException e) {
            log.warn("Unable to retrieve localhost");
        }

        return null;
    }
通过源码可以看出，该工具类会获取所有网卡，依次进行遍历，取ip地址合理、索引值最小且不在忽略列表的网卡的ip地址作为结果。如果仍然没有找到合适的IP, 那么就将InetAddress.getLocalHost()做为最后的fallback方案。

解决方案
忽略指定网卡
通过上面源码分析可以得知，spring cloud肯定能配置一个网卡忽略列表。通过查文档资料得知确实存在该属性：

spring.cloud.inetutils.ignored-interfaces[0]=eth0 # 忽略eth0, 支持正则表达式
因此，第一种方案就是通过配置application.properties让应用忽略无效的网卡。

配置host
当网查遍历逻辑都没有找到合适ip时会走JDK的InetAddress.getLocalHost()。该方法会返回当前主机的hostname, 然后会根据hostname解析出对应的ip。因此第二种方案就是配置本机的hostname和/etc/hosts文件，直接将本机的主机名映射到有效IP地址。

手工指定IP(推荐)
添加以下配置：

指定此实例的ip
eureka.instance.ip-address=
注册时使用ip而不是主机名
eureka.instance.prefer-ip-address=true