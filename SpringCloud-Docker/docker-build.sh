#!/bin/bash

#### docker 构建脚本
##背景：jenkins部署在容器中，无法利用docker执行操作，通过ssh将jar包和当前路径下的dockerfile推送到指定服务器目录上，使用脚本进行构建
# 1. 本地已安装docker，docker push的前提需要当前服务器已经登陆到容器私服如harbor(docker login)
# 2. 同级目录下必须存在Dockerfile文件和需要打包的springboot服务jar包
# 参数说明 $1服务jar名称,$2镜像版本号
## 执行示例： ./docker-build.sh product-service latest

service_name=$1
# 镜像私服仓库/项目，使用harbor作为私服仓库
registry_name=myharbor.com:8088/project
image_tag=$2
jar_file=$service_name.jar
image_info=$registry_name/$service_name:$image_tag

#echo $service_name
#echo $jar_file
#echo $image_info

echo "开始执行镜像构建..."
# 执行docker 构建镜像
build_result=$(docker build -t $image_info --build-arg JAR_FILE=$jar_file .)

# 判断是否构建成功
#echo $build_result
if [[ $build_result =~ "Successfully" ]]; then
  echo "构建成功，镜像信息如下："
  echo `docker images |grep $service_name`
  echo "开始执行镜像推送到私服仓库..."
  push_result=$(docker push $image_info)
  if [[ $push_result =~ "latest: digest" ]]; then
    echo "推送镜像成功："$image_info
    exit 0
  else
    echo "推送镜像失败，失败信息如下："
    echo $push_result
    exit 1
  fi
else
  echo "构建失败，失败信息如下："
  echo $build_result
  exit 1
fi