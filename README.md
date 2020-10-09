

### **1. 简介**
项目使用javaagent+asm框架，在应用运行前修改目标类字节码，以达到捕获运行数据的目的，包括入参、出参、运行堆栈


#### **2. Source Struct**


```
SECAGENT
│
├─gradle
│  └─wrapper
│          gradle-wrapper.jar
│          gradle-wrapper.properties
│
├─libs
│      asm-9.0-beta.jar
│      asm-commons-9.0-beta.jar
│      log4j-1.2.17.jar
│      ojdbc6.jar
│
└─src
    ├─main
    │  │  main.iml
    │  │
    │  ├─java
    │  │  │  AgentDemo.java
    │  │  │  SecAsmTransformer.java
    │  │  │
    │  │  └─SecAgent
    │  │      ├─Conf
    │  │      │      Config.java
    │  │      │      StubConfig.java
    │  │      │
    │  │      ├─Filter
    │  │      │      CopyServletRequestWrapper.java
    │  │      │      CopyServletResponseWrapper.java
    │  │      │      log.md
    │  │      │      SecFilter.java
    │  │      │      SecInstanceProxyFactory.java
    │  │      │      SecInterecptor.java
    │  │      │      SecServletContainerInitializer.java
    │  │      │      SecServletListener.java
    │  │      │
    │  │      ├─Logger
    │  │      │      DefaultLogger.java
    │  │      │      ExceptionLogger.java
    │  │      │      ExceptionLoggerAsync.java
    │  │      │      
    │  │      ├─SecAsm
    │  │      │  ├─Common
    │  │      │  │      CommonAdapter.java
    │  │      │  │      CommonStub.java
    │  │      │  │
    │  │      │  ├─Stub
    │  │      │  │  │  CmdStub.java
    │  │      │  │  │  DownStub.java
    │  │      │  │  │  SerialStub.java
    │  │      │  │  │  SqlStub.java
    │  │      │  │  │  testStub.java
    │  │      │  │  │  TrackStub.java
    │  │      │  │  │  UploadStub.java
    │  │      │  │  │  UrlStub.java
    │  │      │  │  │  XxeStub.java
    │  │      │  │  │
    │  │      │  │  ├─Container
    │  │      │  │  │  └─Tomcat
    │  │      │  │  │          TomcatStub1.java
    │  │      │  │  │
    │  │      │  │  └─Sql
    │  │      │  │      │  MySqlStub.java
    │  │      │  │      │  OracleStub.java
    │  │      │  │      │  
    │  │      │  │      └─Mybatis
    │  │      │  │              MybatisSqlAStub.java
    │  │      │  │              MybatisSqlBStub.java
    │  │      │  │              MybatisValueStub.java
    │  │      │  │
    │  │      │  └─test
    │  │      │          testClassVisitor.java
    │  │      │
    │  │      └─utils
    │  │          │  AgentClassLoader.java
    │  │          │  Common.java
    │  │          │  Pair.java
    │  │          │  ParamsInfo.java
    │  │          │  ReqInfo.java
    │  │          │  ReqInfoState.java
    │  │          │  ReqLocal.java
    │  │          │  Resources.java
    │  │          │
    │  │          ├─DefaultLoggerHelper
    │  │          │      DefaultLogConsoleHandler.java
    │  │          │      DefaultLogFileHandler.java
    │  │          │      DefaultLogFormat.java
    │  │          │      DefaultLogger.java
    │  │          │      DefaultLogManager.java
    │  │          │      
    │  │          ├─Encoder
    │  │          │      Base64.java
    │  │          │
    │  │          ├─HttpClientHelper
    │  │          │      HttpClientHelper.java
    │  │          │
    │  │          ├─HttpClientLoggerHelper
    │  │          │      HttpLogger.java
    │  │          │
    │  │          └─SqlLoggerHelper
    │  │                  MySqlConnectionPool.java
    │  │                  MysqlLogger.java
    │  │
    │  └─resources
    │      │  log4j.properties
    │      │
    │      ├─config
    │      │      config.properties
    │      │
    │      └─META-INF
    │          └─services
    │                  javax.servlet.ServletContainerInitializer
    │
    └─test
        │  test.iml
        │
        ├─java
        │  │  AgentTargetSample.java
        │  │  Test.java
        │  │
        │  └─java
        │      └─io
        └─resources
                test.xml
```

#### **3. how to build**
use `gradlew jar` build

