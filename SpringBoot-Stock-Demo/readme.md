**Jmeter+Springboot+Redisson分布式锁并发订单操作(下单、取消单、完成单、加库存)**<br>
涉及知识点：<br>
   > java+springboot+mybatis开发<br>
   > redis分布式锁+Redisson客户端<br>
   > Jmeter各种骚操作：用户变量、随机取值、jdbc操作、if else操作、循环、控制器、beanshell断言等等<br> 
1. 环境工具：<br>
   idea、jmeter<br>
   jdk1.8、maven、mysql、redis<br>
   三台服务器：两个4C16G服务节点+一个台nginx(淘宝的tengine-2.3.0)节点<br>
2. 思路概要：<br>
   >(1) 主要提供四个接口：下单、取消、出库、添加库存，四种操作在操作库存表t_stock_demo行的时候都需要添加Redis的锁，使用：``Future<Boolean> res = fairLock.tryLockAsync(50, 10, TimeUnit.SECONDS);``<br>
   >(2) 另外取消和出库，因为是用Jmeter直接查询数据库获取可用的订单数量，为防止统一订单重复操作在RestSevice层使用订单号orderNo做了一层Redis分布式锁，订单已在操作直接返回结果。<br>
   >(3) 使用jmeter的jdbc操作+函数、随机数获取已确认的订单结合if控制器判断结果，进行取消和出库操作<br>
   >(4) 划重点：使用分布式锁和本地事物，一定要**先提交事物再释放锁、先提交事物再放锁、先提交事物再放锁**<br>
3. SQL、jmeter脚本、jar包启动脚本请到[doc](https://github.com/Xlinlin/SpringCloud-Demo/tree/master/SpringBoot-Stock-Demo/doc)目录查看。
4. 操作指南：
   >(1) git clone https://github.com/Xlinlin/SpringCloud-Demo   <br>
   >(2) cd SpringCloud-Demo/SpringBoot-Stock-Demo  <br>
   >(3) 配置数据mysql和redis配置，application.yml文件，(自行准备mysql、redis环境) <br>
   >(4) mvn install <br>
   >(5) 拷贝stock_demo.jar和doc/bootstrap.sh到 linux服务器(自行准备java环境)上 <br>
   >(6) 适当修改bootstrap.sh脚本目录，保持与springboot包在同一目录，直接执行脚本：``./bootstrap start`` <br>
   >(7) 查看进程、端口是否启动：``jps 或 ps -ef|grep stock_demo 或 lsof -i:7878``<br>
   >(8) 配好nginx跳转
   >(9) 下载[jmeter](http://jmeter.apache.org/download_jmeter.cgi) ,解压进入jmeter目录，双击：ApacheJMeter<br>
   >(10) 文件->打开->找到doc下的[.jmx](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringBoot-Stock-Demo/doc/stock_demo_jmeter.jmx)文件，大概的画面：![](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringBoot-Stock-Demo/doc/stock_demo_jmeter.jpg)<br>
   >(11) 修改远程服务器地址信息为你的nginx服务<br>
   >(12) 修改你的数据地址，此处需要将mysql的驱动jar包引入jmeter/lib目录下<br>
   >(13) 线程、参数、请求调整好后，然后点击启动(Ctrl+R)<br>
   部分截图：
   >(14) 后台日志![](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringBoot-Stock-Demo/doc/sever_console_log.jpg)
   >(15) 库存表![](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringBoot-Stock-Demo/doc/stock_query.jpg)
   >(16) 订单表![](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringBoot-Stock-Demo/doc/order_query.jpg)
   