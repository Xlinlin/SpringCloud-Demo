配置中心


仓库中的配置文件会被转换成web接口，访问可以参照以下的规则：

/{application}/{profile}[/{label}]
/{application}-{profile}.yml
/{label}/{application}-{profile}.yml
/{application}-{profile}.properties
/{label}/{application}-{profile}.properties


启动后访问：
http://localhost:1110/springcloud/dev

返回结果：
{"name":"springcloud",
"profiles":["dev"],
"label":null,
"version":"8b9b92ac572f1ae18d8fa52b5c7d1dc489b6cb18",
"state":null,
"propertySources":[
{"name":"https://github.com/Xlinlin/spring-cloud-demo.git/SpringCloud-Configure/consumer/springcloud-dev.properties",
"source":{"springcloud.configure.test":"hello-dev"}}
]}