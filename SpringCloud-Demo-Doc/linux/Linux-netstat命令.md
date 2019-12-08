**Linux之netstat命令**

1. 需求：<br>
   在停止Linux上一个进程之前，需要该进程无客户端连接，才能停止该服务<br>
   比如：``kill -15 pid``，不过这个命令还是不保险<br>
   最终还是选择用linux的netstat命令来实现该功能<br>
2. netstat命令介绍：<br>
   语法：<br>
   ``netstat [-acCeFghilMnNoprstuvVwx][-A<网络类型>][--ip]``<br>
   参数说明：
   ```$xslt
    -a或--all 显示所有连线中的Socket。
    -A<网络类型>或--<网络类型> 列出该网络类型连线中的相关地址。
    -c或--continuous 持续列出网络状态。
    -C或--cache 显示路由器配置的快取信息。
    -e或--extend 显示网络其他相关信息。
    -F或--fib 显示FIB。
    -g或--groups 显示多重广播功能群组组员名单。
    -h或--help 在线帮助。
    -i或--interfaces 显示网络界面信息表单。
    -l或--listening 显示监控中的服务器的Socket。
    -M或--masquerade 显示伪装的网络连线。
    -n或--numeric 直接使用IP地址，而不通过域名服务器。
    -N或--netlink或--symbolic 显示网络硬件外围设备的符号连接名称。
    -o或--timers 显示计时器。
    -p或--programs 显示正在使用Socket的程序识别码和程序名称。
    -r或--route 显示Routing Table。
    -s或--statistice 显示网络工作信息统计表。
    -t或--tcp 显示TCP传输协议的连线状况。
    -u或--udp 显示UDP传输协议的连线状况。
    -v或--verbose 显示指令执行过程。
    -V或--version 显示版本信息。
    -w或--raw 显示RAW传输协议的连线状况。
    -x或--unix 此参数的效果和指定"-A unix"参数相同。
    --ip或--inet 此参数的效果和指定"-A inet"参数相同。
    ```
   参考[菜鸟教程](https://www.runoob.com/linux/linux-comm-netstat.html)<br>
   在此处我们用到的命令：<br>
   ``netstat -an | grep PORT``<br>
   如下图所示，显示所有的socket连接以及连接的客户端信息：<br>
   ![avatar](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringCloud-Demo-Doc/linux/img/netstat-np.png?raw=true)<br>
   没有连接的情况：<br>
   ![avatar](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringCloud-Demo-Doc/linux/img/netstat-np-no-client.png?raw=true)<br>
3. 我们利用这个命令 结合 wc(root) 和 awk命令 获取到当前端口的客户端连接数，命令如下：<br>
   ``netstat -an | grep 7778 |wc | awk '{print $1}'``<br>
   无客户端连接情况：<br>
   ![avatar](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringCloud-Demo-Doc/linux/img/netstat-awk-no-client.png?raw=true)<br>
   有客户端连接情况：<br>
   ![avatar](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringCloud-Demo-Doc/linux/img/netstat-awk.png?raw=true)<br>
   借此我们可以拿到这个客户端连接数，来做**服务自动化发布时**的是否可以停止服务的条件
   
