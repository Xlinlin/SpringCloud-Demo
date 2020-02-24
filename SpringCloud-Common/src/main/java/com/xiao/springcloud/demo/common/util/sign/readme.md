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