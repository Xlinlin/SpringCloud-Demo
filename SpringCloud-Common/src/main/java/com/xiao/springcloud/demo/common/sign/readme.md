提供生成签名串工具类<br>
依赖包：
```html
commons-lang3
commons-collections
commons-codec
slf4j-api
fastjson
```
使用方法方法：<br>
```html
SignUtil.generateSign(Map<String, String> kvParams, String key)
SignUtil.generateSign(Map<String, String> kvParams, String jsonParams, String key)
SignUtil.generateSignByJson(String jsonParams,String key)
SignUtil.generateSign(String content,String key)
```
签名说明:
1. KV参数拼接
2. JSON请求参数，内部会做JSONObject转换，转成json字符串
3. 将kv参数和json参数拼在一起，按ascii排序规则进行排序
4. 对排序后的字符串做HmacSHA1加密

Springboot web应用签名：<br>
1. 包装一个request读取request流数据费
2. 自定义一个filter，处理request中header的sign和appid以及请求参数，通过appid找到对应的appkey通过签名工具生成服务端签名，放到reqeust周往下传
3. 自定义一个注解@DisposeSign，需要签名的接口加上该注解即可
4. AppManagerService管理appid和appKey对应的关系