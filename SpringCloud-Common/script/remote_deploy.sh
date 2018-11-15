#!/bin/bash

## jenkins 自动部远程署脚本，需要配置两个服务器之间免密登录
## 免登陆配置参考：https://blog.csdn.net/u011186019/article/details/51737760?utm_source=blogxgwz4
##jekins 项目 Post Steps设置 启动该脚本，配置如下：
#BUILD_ID=DONTKILLME   -- 表示Jenkins执行完后不杀死该进程，否则会再jenkins执行完后杀死启动的进程
#bash /home/omni-services/remote-deploy/remote-deploy.sh

SERVICE_NAME=appname
REMOTE_SERVICES_HOME=/home/services
REMOTE_SERVICE_IP=目标服务器IP
REMOTE_SERVICE_HOME=$REMOTE_SERVICES_HOME/$SERVICE_NAME
##服务jar
REMOTE_SERVICE_JAR=$REMOTE_SERVICE_HOME/$SERVICE_NAME.jar
##启动脚本
REMOTE_SERVICE_BOOTSTRAP=$REMOTE_SERVICE_HOME/bootstrap.sh
##jenkins编译完成后服务Jar包位置
LOCAL_JAR_PATH=/root/.jenkins/workspace/$SERVICE_NAME-perf/target/$SERVICE_NAME.jar

echo "=============Service name: $SERVICE_NAME"
echo "=============Remote services home: $REMOTE_SERVICES_HOME"
echo "=============Remote server ip: $REMOTE_SERVICE_IP"
echo "=============Remote service home: $REMOTE_SERVICE_HOME"
echo "=============Remote service jar: $REMOTE_SERVICE_JAR"
echo "=============Remote service bootstrap file: $REMOTE_SERVICE_BOOTSTRAP"

## 远程操作，停止并备份原来的服务
echo "-----Stop and Delete remote service: $SERVICE_NAME"
ssh -T root@$REMOTE_SERVICE_IP << remotessh
sh $REMOTE_SERVICE_BOOTSTRAP stop
mv $REMOTE_SERVICE_JAR $REMOTE_SERVICE_HOME/$SERVICE_NAME-`date "+%Y-%m-%d %H:%M:%S"`.jar
exit
remotessh

##复制一份新的服务Jar
echo "-----Scp $LOCAL_JAR_PATH to $REMOTE_SERVICE_HOME"
scp $LOCAL_JAR_PATH root@$REMOTE_SERVICE_IP:$REMOTE_SERVICE_HOME 

##远程启动服务
echo "-----Start remote service"
ssh -T root@$REMOTE_SERVICE_IP << remotessh
sh $REMOTE_SERVICE_BOOTSTRAP start
exit
remotessh

exit
