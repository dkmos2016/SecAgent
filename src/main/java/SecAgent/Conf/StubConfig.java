package SecAgent.Conf;

public class StubConfig {
  public static final String SQL_STUB =
      "com.mysql.cj.jdbc.EscapeProcessor.escapeSQL(Ljava.lang.String;Ljava.util.TimeZone;ZZLcom.mysql.cj.exceptions.ExceptionInterceptor;)Ljava.lang.Object;";

  // bug
  //  public static final String MYSQL_STUB =
  //      "com.mysql.cj.jdbc.StatementImpl.executeInternal(Ljava.lang.String;Z)Z";

  public static final String MYSQL_STUB =
      "com.mysql.cj.jdbc.StatementImpl.checkNullOrEmptyQuery(Ljava.lang.String;)V";

  //  public static final String MYSQL_STUB =
  //
  // "com.mysql.cj.jdbc.EscapeProcessor.escapeSQL(Ljava.lang.String;Ljava.util.TimeZone;ZZLcom.mysql.cj.exceptions.ExceptionInterceptor;)Ljava.lang.Object;";

  //  public static final String ORACLE_STUB =
  //      "oracle.jdbc.driver.OracleStatement.executeInternal(Ljava.lang.String;)Z";

  public static final String ORACLE_STUB =
      "oracle.jdbc.driver.OracleSql.initialize(Ljava.lang.String;)V";

  public static final String MYBATIS_SQLA_STUB =
      "org.apache.ibatis.scripting.xmltags.TextSqlNode.apply(Lorg.apache.ibatis.scripting.xmltags.DynamicContext;)Z";

  public static final String MYBATIS_SQLB_STUB =
      "org.apache.ibatis.scripting.xmltags.DynamicContext.appendSql(Ljava.lang.String;)V";

  public static final String MYBATIS_VALUE_STUB =
      "org.apache.ibatis.scripting.xmltags.TextSqlNode$BindingTokenParser.handleToken(Ljava.lang.String;)Ljava.lang.String;";

  //    public final static String SQL_STUB = "java.sql.Statement.execute(Ljava.lang.String;)Z";
  public static final String EXEC_STUB =
      "java.lang.ProcessImpl.start([Ljava.lang.String;Ljava.util.Map;Ljava.lang.String;[Ljava.lang.ProcessBuilder$Redirect;Z)Ljava.lang.Process;";
  public static final String DOWN_STUB = "java.io.FileInputStream.<init>(Ljava.io.File;)V";
  public static final String UPLOAD_STUB = "java.io.FileOutputStream.<init>(Ljava.io.File;Z)V";
  //    public static final String SPRING_URL_STUB =
  // "org.springframework.web.servlet.DispatcherServlet.doService(Ljavax.servlet.http.HttpServletRequest;Ljavax.servlet.http.HttpServletResponse;)V";\
  public static final String TOMCAT_URL_STUB =
      "javax.servlet.http.HttpServlet.service(Ljavax.servlet.http.HttpServletRequest;Ljavax.servlet.http.HttpServletResponse;)V";

  public static final String PAFA5_HANDLE_REQUEST = "com.pingan.pafa.papp.sar.SARContextBean.handleRequest(Lcom/paic/pafa/app/dto/ServiceRequest;)Lcom/paic/pafa/app/dto/ServiceResponse;";
  public static final String PAFA5_HANDLE_WEB_REQUEST = "com.pingan.pafa.papp.sar.SARContextBean.handleWebRequest(Ljavax/servlet/http/HttpServletRequest;Ljavax/servlet/http/HttpServletResponse;)Z";

//  public static final String DUBBO_STUB = "com.alibaba.dubbo.rpc.RpcInvocation.<init>(Ljava.lang.String;[Ljava.lang.Class;[Ljava.lang.Object;Ljava.util.Map;Lcom.alibaba.dubbo.rpc.Invoker;)V";

  public static final String DUBBO_STUB = "com.alibaba.dubbo.remoting.transport.DecodeHandler.received(Lcom.alibaba.dubbo.remoting.Channel;Ljava.lang.Object;)V";
  // doing

  public static final String XXE_STUB =
      "com.sun.org.apache.xerces.internal.impl.XMLEntityManager$RewindableInputStream.<init>(Lcom.sun.org.apache.xerces.internal.impl.XMLEntityManager;Ljava.io.InputStream;)V";
  //  public static final String
  // XXE_STUB="com.sun.org.apache.xerces.internal.impl.XMLVersionDetector.determineDocVersion(Lcom.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;)S";

  // todo
  public static final String SSRF_STUB = "java.io.FileOutputStream.<init>(Ljava.io.File;Z)V";

  public static final String TOMCAT_STUB =
      "org.apache.tomcat.util.http.fileupload.FileUploadBase.parseRequest(Lorg.apache.tomcat.util.http.fileupload.RequestContext;)Ljava.util.List;";

  public static String[] exclude_classes;
  public static String[] exclude_methods;
  public static String[] include_classes;
  public static String[] include_methods;

  static {
    exclude_classes =
        new String[] {
          "MethodHandleImpl", "ClassValue", "ExceptionInInitializerError",
        };

    exclude_methods = new String[] {"main", "<clinit>"};
    include_classes =
        new String[] {
          "java.io",
          // xxe
          "com.sun.org.apache.xerces.internal.impl.XMLEntityManager",
          "com.sun.org.apache.xerces.internal.impl.XMLEntityManager$RewindableInputStream",
          // url
          "javax.servlet.http.HttpServlet",
          // down
          //          "java.io.FileInputStream",
          // upload
          //          "java.io.FileOutputStream",
          // exec
          "java.lang.ProcessImpl",

          // mysql
          "com.mysql.cj.jdbc.StatementImpl",

          /* ignore oracle because of ojdbc6 exception */
          "oracle.jdbc.driver.OracleStatement",
          //            "oracle.jdbc.driver.OracleResultSetImpl",

          /* mybatis */
          "org.apache.ibatis.mapping.BoundSql",
          "org.apache.ibatis.scripting.xmltags.OgnlCache",
          "org.apache.ibatis.ognl.Ognl",
          "org.apache.ibatis.parsing.GenericTokenParser",
          //            "org.apache.ibatis.mapping.SqlSource",

          // tomcat
          "org.apache.tomcat.util.http.fileupload.FileUploadBase",
          "oracle.jdbc.driver.OracleSql",

          //          "org.apache.ibatis.mapping.MappedStatement",
          //          "org.apache.ibatis.scripting.xmltags.MixedSqlNode",
          "org.apache.ibatis.scripting.xmltags.TextSqlNode",
          "org.apache.ibatis.scripting.xmltags.TextSqlNode$BindingTokenParser",
          "org.apache.ibatis.scripting.xmltags.DynamicContext",
//
//                "com.pingan.pafa.papp.sar.SARContextBean",
//                "com.pingan.pafa.papp.esa.annotation.MethodESADispatcher",
//                "com.pingan.pafa.papp.sar.context.DefaultSARDispatcherBean",
          "com.alibaba.dubbo.rpc.RpcInvocation",
          "com.alibaba.dubbo.remoting.transport.DecodeHandler",
        };

    include_methods =
        new String[] {
          //          "len.test.show2",
          //          "java.lang.SecurityManager.checkWrite(Ljava.lang.String;)V",
          //          "java.lang.Runtime.exec",
          //          "java.sql.Statement",
          //          "java.lang.UNIXProcess",
          //          "com.sun.org.apache.xerces.internal.impl.XMLVersionDetector",
          //
          // "com.sun.org.apache.xerces.internal.impl.XMLEntityManager$RewindableInputStream.<init>",
          //                          "java.sql.Statement.execute",
          //                    "com.mysql.cj.jdbc.StatementImpl",
          //
          // "com.mysql.cj.jdbc.EscapeProcessor.escapeSQL(Ljava.lang.String;Ljava.util.TimeZone;ZZLcom.mysql.cj.exceptions.ExceptionInterceptor;)Ljava.lang.Object;",
          //                          "oracle.jdbc.driver.OracleStatement.executeInternal",
          //                "oracle.jdbc.driver.OracleStatement.execute",

          // mybatis
          //                "org.apache.ibatis.mapping.BoundSql",
          //                "org.apache.ibatis.mapping.SqlSource",

          //
          // "org.apache.tomcat.util.http.fileupload.FileUploadBase.parseRequest(Lorg.apache.tomcat.util.http.fileupload.RequestContext;)Ljava.util.List;",
          //          "org.apache.ibatis.scripting.xmltags.OgnlCache.parseExpression",

          // sql in xxMapper.xml value(${id}...)
          //                    "org.apache.ibatis.ognl.Ognl.getValue",

          // sql in xxMapper.xml (process twice, #/$)
          //                    "org.apache.ibatis.parsing.GenericTokenParser.parse",

          //                "oracle.jdbc.driver.OracleSql.initialize",

          //          "org.apache.ibatis.mapping.MappedStatement.getBoundSql",
          //          "org.apache.ibatis.scripting.xmltags.MixedSqlNode.apply",
          //          "org.apache.ibatis.scripting.xmltags.TextSqlNode.apply"
          //        "org.apache.ibatis.scripting.xmltags.DynamicContext.appendSql",
          //
          // "org.apache.ibatis.scripting.xmltags.TextSqlNode$BindingTokenParser.handleToken",
//                "com.pingan.pafa.papp.sar.SARContextBean.handle",
//                "com.pingan.pafa.papp.esa.annotation.MethodESADispatcher.handle",
//                "com.pingan.pafa.papp.sar.context.DefaultSARDispatcherBean.handle",

                // sender
//                "com.alibaba.dubbo.rpc.RpcInvocation",
//                "com.alibaba.dubbo.rpc.RpcInvocation.<init>(Ljava.lang.String;[Ljava.lang.Class;[Ljava.lang.Object;Ljava.util.Map;Lcom.alibaba.dubbo.rpc.Invoker;)V",

                // receiver
//                "com.alibaba.dubbo.remoting.transport.DecodeHandler.received",
        };
  }

  public static boolean isExcludedClass(String src) {
    for (String clazz : exclude_classes) {
      if (src.contains(clazz.replace('.', '/'))) {
        return true;
      }
    }
    return false;
  }

  public static boolean isExcludedMethod(String src) {
    for (String method : exclude_methods) {
      if (src.contains(method)) {
        return true;
      }
    }
    return false;
  }

  public static boolean isIncludedClass(String src) {
    for (String clazz : include_classes) {
      if (src.replace("/", ".").equals(clazz)) {
        return true;
      }
    }
    return false;
  }

  public static boolean isIncludedMethod(String src) {
    for (String method : include_methods) {
      if (src.replace("/", ".").startsWith(method)) {
        return true;
      }
    }
    return false;
  }
}
