**Docker + SpringBoot + Maven 构建发布到远程仓库**
_环境：Aliyun Centos 7 Windows10 IDEA Maven3.5.4_

1. Docker安装:<br>
[参考阿里云的安装手册](https://yq.aliyun.com/articles/110806?spm=5176.8351553.0.0.7b301991WYZA2d)<br>
    1.1 **CentOS 7 (使用yum进行安装)**<b>
    ```$xslt
    # step 1: 安装必要的一些系统工具
    sudo yum install -y yum-utils device-mapper-persistent-data lvm2
    # Step 2: 添加软件源信息
    sudo yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
    # Step 3: 更新并安装 Docker-CE
    sudo yum makecache fast
    sudo yum -y install docker-ce
    # Step 4: 开启Docker服务
    sudo service docker start
    
    # 注意：
    # 官方软件源默认启用了最新的软件，您可以通过编辑软件源的方式获取各个版本的软件包。例如官方并没有将测试版本的软件源置为可用，你可以通过以下方式开启。同理可以开启各种测试版本等。
    # vim /etc/yum.repos.d/docker-ce.repo
    #   将 [docker-ce-test] 下方的 enabled=0 修改为 enabled=1
    #
    # 安装指定版本的Docker-CE:
    # Step 1: 查找Docker-CE的版本:
    # yum list docker-ce.x86_64 --showduplicates | sort -r
    #   Loading mirror speeds from cached hostfile
    #   Loaded plugins: branch, fastestmirror, langpacks
    #   docker-ce.x86_64            17.03.1.ce-1.el7.centos            docker-ce-stable
    #   docker-ce.x86_64            17.03.1.ce-1.el7.centos            @docker-ce-stable
    #   docker-ce.x86_64            17.03.0.ce-1.el7.centos            docker-ce-stable
    #   Available Packages
    # Step2 : 安装指定版本的Docker-CE: (VERSION 例如上面的 17.03.0.ce.1-1.el7.centos)
    # sudo yum -y install docker-ce-[VERSION]
    ```
   1.2 安装校验，输入如下命令，会显示docker信息<br>
   ``docker version``
 2. Docker私服仓库搭建(无登录密码)<br>
   _环境：PSW(Private service warehouse私服仓库)，使用registry V2方式安装，域名假设域名使用reg.winner.com_<br>
   2.1 host域名映射：echo '192.168.206.210 reg.winner.com'>> /etc/hosts <br>
   2.2 环境准备-https证书(openssl证书生成，可能需要安装openssl):<br>
   Key:
   ````$xslt
    mkdir -p ~/certs
    cd ~/certs
    openssl genrsa -out reg.winner.com.key 2048
   ````
   Crt文件：
   ```$xslt
    openssl req -newkey rsa:4096 -nodes -sha256 -keyout reg.winner.com.key -x509 -days 365 -out reg.winner.com.crt
   ```
   一些要填的信息:
   ```$xslt
    Country Name (2 letter code) [XX]:CN
    # 国家
    State or Province Name (full name) []:JS
    # 省会
    Locality Name (eg, city) [Default City]:NJ
    # 城市
    Organization Name (eg, company) [Default Company Ltd]:ITMUCH
    # 组织(公司)
    Organizational Unit Name (eg, section) []:ITMUCH
    # 组织单位(部门)
    Common Name (eg, your name or your server's hostname) []:reg.winner.com 
    # 域名
    Email Address []:eacdy0000@126.com
    # 邮件
   ```
   Tips:自签名证书不受docker信任,需要添加到Docker根证书中,Centos 7的存放路径：```/etc/docker/certs.d/域名``
   ```$xslt
   mkdir -p /etc/docker/certs.d/reg.winner.com
   cp ~/certs/reg.winner.com.crt /etc/docker/certs.d/reg.winner.com/
   ```
   重启docker: ``service docker restart``<br>
   2.3 私服仓库的启动：<br>
   ``cd ~``
   ```$xslt
    docker run -d -p 443:5000 --restart=always --name registry -v `pwd`/certs:/certs -v /opt/docker-image:/opt/docker-image -e STORAGE_PATH=/opt/docker-image -e REGISTRY_HTTP_TLS_CERTIFICATE=/certs/reg.winner.com.crt -e REGISTRY_HTTP_TLS_KEY=/certs/reg.winner.com.key registry:2
   ```
   ```$xslt
       一些参数解释：
       `pwd`/certs:/certs --- 将当前目录(cd ~)下的certs挂载到容器的certs目录
       -v /opt/docker-image:/opt/docker-image --指定存储镜像路径，防止私有仓库容器被删，镜像也丢失
       -e REGISTRY_HTTP_TLS_CERTIFICATE和-e REGISTRY_HTTP_TLS_KEY 指定证书路径
   ```
   2.4 开放远程API，以供我们IDEA+MAVEN远程push镜像到私服仓库:<br>
   1). docker默认是没有开启Remote API的，需要我们手动开启
   编辑``/lib/systemd/system/docker.service``文件。_(tips:该文件路径不一定在这里，本案例是在这个目录)_<br>
   修改如下内容：
   ```$xslt
    [Service]
    Type=notify
    # the default is not to use systemd for cgroups because the delegate issues still
    # exists and systemd currently does not support the cgroup feature set required
    # for containers run by docker
    ExecStart=/usr/bin/dockerd -H unix:///var/run/docker.sock -H tcp://0.0.0.0:2375
    #ExecStart=/usr/bin/dockerd -H fd://
    #ExecStart=/usr/bin/dockerd -H unix://
    ExecReload=/bin/kill -s HUP $MAINPID
   ```
   重点关注:``ExecStart``这个参数<br>
   2). 重启服务<br>
   ``sudo systemctl daemon-reload``<br>
   ``sudo service docker restart``<br>
   3). 验证<br>
   浏览器输入``192.168.206.210:2375/images/json``能返回宿主机的所有信息，如镜像信息：
   ```$xslt
    [{"Containers":-1,"Created":1548890077,"Id":"sha256:d0eed8dad114db55d81c870efb8c148026da4a0f61dc7710c053da55f9604849","Labels":null,"ParentId":"","RepoDigests":["registry@sha256:870474507964d8e7d8c3b53bcfa738e3356d2747a42adad26d0d81ef4479eb1b"],"RepoTags":["registry:2"],"SharedSize":-1,"Size":25779681,"VirtualSize":25779681}]
   ```
   更多参考资料[Docker开启Remote API](https://blog.csdn.net/yuanlaijike/article/details/80913096)<br>
3. IDEA + Maven + Springboot + Dockerfile发布到远程仓库<br>
  _idea安装Docker Integration 插件，在setting->build->docker->tcp socket中配置Docker信息，选择一下私服仓库是所生成的.crt证书文件_<br>
  [IDEA docker插件其他参考资料](https://www.jianshu.com/p/6ce91051d24c)<br>
  3.1 windwos环境添加``DOCKER_HOST``环境变量:
  ```$xslt
    DOCKER_HOST
    tcp://192.168.206.210:2375
   ```
  3.2 pom关键配置配置：
  ```$xslt
     <!-- maven docker 插件 -->
                <plugin>
                    <groupId>com.spotify</groupId>
                    <artifactId>docker-maven-plugin</artifactId>
                    <version>1.0.0</version>
    
                    <!--将插件绑定在某个phase执行-->
                    <executions>
                        <execution>
                            <id>build-image</id>
                            <!--将插件绑定在package这个phase上。用户只需执行mvn package ，就会自动执行mvn docker:build-->
                            <phase>package</phase>
                            <goals>
                                <goal>build</goal>
                            </goals>
                        </execution>
                    </executions>
    
                    <configuration>
                        <!-- imageName一定要符合规则[a-z0-9-_.] -->
                        <imageName>${project.artifactId}:${project.version}</imageName>
                        <!--指定标签-->
                        <imageTags>
                            <imageTag>latest</imageTag>
                        </imageTags>
                        <!-- 指定 Dockerfile 路径  ${project.basedir}：项目根路径下-->
                        <dockerDirectory>${project.basedir}</dockerDirectory>
                        <!--指定远程 docker api地址-->
                        <dockerHost>http://192.168.206.210:2375</dockerHost>
                        <resources>
                            <resource>
                                <directory>${project.build.directory}</directory>
                                <include>${project.build.finalName}.jar</include>
                            </resource>
                        </resources>
                    </configuration>
                </plugin>
  ```
  3.3 Dockerfile配置:
  ```$xslt
    ## 基于java 8镜像
    FROM java:8
    
    ## 将本地文件挂在到当前容器
    VOLUME /tmp
    
    ## 拷贝文件内容
    ADD omni-zipkin-server.jar zipkin-server.jar
    RUN bash -c 'touch /zipkin-server.jar'
    
    ## 开放端口
    EXPOSE 8818
    
    ## 容器启动后命令
    ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/zipkin-server.jar"]
  ```
  3.4 执行maven package 打包 输出信息：
  ```$xslt
   ......省略......
   ......省略......
     ---> Running in e29385dab518
    Removing intermediate container e29385dab518
     ---> 02cd42534504
    Step 5/6 : EXPOSE 8818
    
     ---> Running in 7f88b962b174
    Removing intermediate container 7f88b962b174
     ---> d34b82c1092d
    Step 6/6 : ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/zipkin-server.jar"]
    
     ---> Running in 3062e50da1ea
    Removing intermediate container 3062e50da1ea
     ---> 006a803605d3
    ProgressMessage{id=null, status=null, stream=null, error=null, progress=null, progressDetail=null}
    Successfully built 006a803605d3
    Successfully tagged omni-zipkin-server:0.0.1-SNAPSHOT
    [INFO] Built omni-zipkin-server:0.0.1-SNAPSHOT
    [INFO] Tagging omni-zipkin-server:0.0.1-SNAPSHOT with latest
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
   ```
   3.5 到私服仓库查看镜像：
   ```$xslt
    [root@iZwz95nk600g8wdsa3pbxvZ ~]# docker images
    REPOSITORY           TAG                 IMAGE ID            CREATED             SIZE
    omni-zipkin-server   0.0.1-SNAPSHOT      006a803605d3        42 minutes ago      763MB
    omni-zipkin-server   latest              006a803605d3        42 minutes ago      763MB
    registry             2                   d0eed8dad114        29 hours ago        25.8MB
    java                 8                   d23bdf5b1b1b        2 years ago         643MB
   ```