#!/bin/bash

## jenkins 自动部署脚本   jekins 项目 Post Steps设置 启动该脚本，配置如下：
#  BUILD_ID=DONTKILLME
#  bash /home/omni-services/remote-deploy/auto_deploy.sh

SERVICE_NAME=content-management-service
JARPATH=/root/.jenkins/workspace/$SERVICE_NAME-perf/target/$SERVICE_NAME.jar
echo "service name:$SERVICE_NAME"
SERVICE_HOME=/home/omni-services/$SERVICE_NAME
JARFILE=$SERVICE_HOME/$SERVICE_NAME.jar
BOOTSTRAP_FILE=$SERVICE_HOME/bootstrap.sh

echo "=====service name: $SERVICE_NAME"
echo "=====service home: $SERVICE_HOME"
echo "=====service resouce jar path: $JARPATH"
echo "=====service bootstrap file: $BOOTSTRAP_FILE"
echo "=====service jar: $JARFILE"

## 停止原来的服务
echo "-----Stop service"
bash $BOOTSTRAP_FILE stop

## 备份原来的Jar包
echo "-----Bach source jar file: $JARFILE"
mv $JARFILE $SERVICE_HOME/$SERVICE_NAME-`date "+%Y-%m-%d %H:%M:%S"`.jar

## 复制新包
echo "-----Copy $JARPATH to $SERVICE_HOME"
cp $JARPATH $SERVICE_HOME
COPYRULST=$?

## 复制OK，开始启动
if [ $COPYRULST -eq 0 ]; 
then
    echo "-----Copy success,wait start service....."
    bash $BOOTSTRAP_FILE start
    RETVAL=$?
    if [ $RETVAL -eq 0 ];
    then
       echo "-----$SERVICE_NAME started!"
    else
       echo "-----$SERVICE_NAME start failed!"
    fi
else
    echo "-----Not exist $SERVICE_NAME jar"
    exit 1
fi


