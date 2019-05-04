1. MQTT(activemq)+NGINX实现 java、android、IOS、web、小程序消息推送
2. 小程序需要nginx配置wss，通过nginx跳转到activemq ws服务，详情[文档参考](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringCloud-Demo-Doc/%E5%85%B6%E4%BB%96%E4%B8%AA%E4%BA%BA%E6%80%BB%E7%BB%93/%E5%BE%AE%E4%BF%A1%E5%B0%8F%E7%A8%8B%E5%BA%8FMQTT%E5%8D%8F%E8%AE%AE%E9%80%9A%E4%BF%A1.docx)
3. nginx配置[参考](https://github.com/Xlinlin/SpringCloud-Demo/tree/master/SpringCloud-MQTT/doc),其中wss.conf文件为 小程序跳转的wss服务，mqtt.conf为mqtt的tcp配置，nginx.conf为主配置
4. activemq下载源码，在其基础上做简单鉴权处理，在org.apache.activemq.security.SimpleAuthenticationBroker上做简单封装，比如做密码MD5加密等
5. nginx ssl配置暂未实现 