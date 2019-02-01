## 基于java 8镜像
FROM java:8

## 将本地文件挂在到当前容器
VOLUME /tmp

## 拷贝文件内容
ADD omni-zipkin-server.jar zipkin-server.jar
RUN bash -c 'touch /zipkin-server.jar'

## 开放端口
EXPOSE 8100

## 容器启动后命令
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/zipkin-server.jar"]
