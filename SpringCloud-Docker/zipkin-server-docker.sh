#!/bin/bash

REMOTE_SERVICE_IP=ssh IP
SERVICE_DOCKER_IAMGE_NAME=SpringCloud-Docker
SERVICE_DOCKER_PORT=8818
SERVICE_DOCKER_TAG=latest

## docker root用户安装，普通用户admin执行无权限解决：
#sudo groupadd docker     #添加docker用户组
#sudo gpasswd -a $USER docker     #将登陆用户加入到docker用户组中
#newgrp docker     #更新用户组
#docker ps    #测试docker命令是否可以使用sudo正常使用


## 远程启动docker项目
CONTAINER_ID=`ssh root@$REMOTE_SERVICE_IP docker ps -f ancestor=$SERVICE_DOCKER_IAMGE_NAME | awk 'NR > 1 {print $1}'`
echo "-----启动开始启动远程服务"
ssh -T root@$REMOTE_SERVICE_IP << remotessh
docker -v
if [ $? -eq 0 ]; then
  if [ -n "$CONTAINER_ID" ]; then
     echo "容器已存在,容器ID：$CONTAINER_ID,即将停止容器..."
     docker stop $CONTAINER_ID
     echo "容器已停止，即将重新启动..."
     docker run -p $SERVICE_DOCKER_PORT:$SERVICE_DOCKER_PORT -d $SERVICE_DOCKER_IAMGE_NAME:$SERVICE_DOCKER_TAG
  else
     echo "即将启动..."
     docker run -p $SERVICE_DOCKER_PORT:$SERVICE_DOCKER_PORT -d $SERVICE_DOCKER_IAMGE_NAME:$SERVICE_DOCKER_TAG
  fi
else
  echo "检测到未安装docker，请手动安装docker"
fi
exit
remotessh
