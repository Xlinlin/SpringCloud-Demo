1. 配置管理平台<br>
访问地址：http://localhost:9002

2. 新增spring session+ spring security +  jwt简单鉴权<p>
   实现方案简要说明：<p>
  >> 1). 后端实现主要参考``com.xiao.custom.config.web.auth.config.WebSecurityConfig``，浏览器交互需要用上session<p>
  >> 2). 前端实现思路，如果是浏览器正常session交互，如果是ajax请求，需要在请求前加上jwt的token，代码片段：<p>
   ```$xslt
            beforeSend: function (xhr) {
                if ( this.loadArea ){
                    this.loadArea.$set(this.loadArea,'loading',true)
                }else{
                    this.showLoad && App.showLoadding(null, null, xhr);
                }
                this.beforeCallback && this.beforeCallback.call(this,xhr);
                // 设置token 本地获取token  关键部分
                var token = window.localStorage.getItem('M-Auth-Token');
                xhr.setRequestHeader('M-Auth-Token', token);
            },
   ```
   >> 3). 使用到的相关的pom配置<p>
   ```$xslt
    <!-- jwt + security -->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-security</artifactId>
                <version>1.5.9.RELEASE</version>
            </dependency>
            <dependency>
                <groupId>org.springframework.security</groupId>
                <artifactId>spring-security-jwt</artifactId>
                <version>1.0.9.RELEASE</version>
            </dependency>
            <dependency>
                <groupId>io.jsonwebtoken</groupId>
                <artifactId>jjwt</artifactId>
                <version>0.9.0</version>
            </dependency>
            <dependency>
                <groupId>org.springframework.session</groupId>
                <artifactId>spring-session</artifactId>
            </dependency>
   ```
