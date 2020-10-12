

## Change Logs:

### **【1.0.0.1】**

- 修复bug
- 增加getQueries埋点
- 优化logger
- Common配置字段加入注释
- 添加开源LICENSE

### **【1.0.0】**

- 使用动态代理方式实现HttpRequest.getInputStream多次读取
- 使用ServletContainerInitializer实现Filter，结合动态代理实现InputStream多次读取
- 自定义日志模块，实现logging
- 自定义数据库连接池
- 自定义httpclient，实现Get/Post方法推送数据
- 通过配置文件的方式实现日志存放位置配置、数据库配置以及连接池配置
- 封装整合字节码操作，以减少重复字节码操作
- 使用ThreadLocal实现全局访问
- 使用反射方式，并封装字节码操作接口，实现异常处理、埋点数据收集
- 拆分配置，实现埋点配置和常用配置分离