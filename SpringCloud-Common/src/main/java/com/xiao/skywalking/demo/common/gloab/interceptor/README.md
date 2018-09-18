springcloud fegin内部交互异常统一处理

原理：
1. 服务端抛出统一异常，由DefaultControllerAdvice方法捕捉封装，期需要将http response的响应头设置为500
2. 客户端自定义解码器，对异常进行解码，由CommonFeignErrorDecoder进行对异常处理


服务端客户端交互部分对象传输为空：
  自定义httpmessageconvert，由FastjsonConfig类处理，添加fastjson转换