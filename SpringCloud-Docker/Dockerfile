## 基于java 8镜像
FROM java:8

## 将本地文件挂在到当前容器
VOLUME /tmp

## 拷贝文件内容
ADD SpringCloud-Docker.jar SpringCloud-Docker.jar
RUN bash -c 'touch /SpringCloud-Docker.jar'

## 开放端口
EXPOSE 8100

## 容器启动后命令
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/SpringCloud-Docker.jar"]
