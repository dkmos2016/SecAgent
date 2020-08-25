package SecAgent.Conf;

public class Config {

  public final static String SQL_STUB = "com.mysql.cj.jdbc.EscapeProcessor.escapeSQL(Ljava.lang.String;Ljava.util.TimeZone;ZZLcom.mysql.cj.exceptions.ExceptionInterceptor;)Ljava.lang.Object;";
  public final static String SQL_STUB2 = "com/mysql/jdbc/StatementImpl";
  //    public final static String SQL_STUB = "java.sql.Statement.execute(Ljava.lang.String;)Z";
  public static final String EXEC_STUB = "java.lang.ProcessImpl.start([Ljava.lang.String;Ljava.util.Map;Ljava.lang.String;[Ljava.lang.ProcessBuilder$Redirect;Z)Ljava.lang.Process;";
  public static final String DOWN_STUB = "java.io.FileInputStream.<init>(Ljava.io.File;)V";
  public static final String UPLOAD_STUB = "java.io.FileOutputStream.<init>(Ljava.io.File;Z)V";
  //    public static final String SPRING_URL_STUB = "org.springframework.web.servlet.DispatcherServlet.doService(Ljavax.servlet.http.HttpServletRequest;Ljavax.servlet.http.HttpServletResponse;)V";\
  public static final String SPRING_URL_STUB = "javax.servlet.http.HttpServlet.service(Ljavax.servlet.http.HttpServletRequest;Ljavax.servlet.http.HttpServletResponse;)V";
  // todo
  public static final String SSRF_STUB = "java.io.FileOutputStream.<init>(Ljava.io.File;Z)V";
  public static String[] exclude_classes;
  public static String[] exclude_methods;
  public static String[] include_classes;
  public static String[] include_methods;

  static {
    exclude_classes = new String[]{
      "MethodHandleImpl", "ClassValue", "ExceptionInInitializerError",
    };

    exclude_methods = new String[]{"main", "<clinit>"};
    include_classes = new String[]{
      "java.io",
      "java.sql.Statement"
    };
    include_methods = new String[]{
      "len.test.show2",
      "java.lang.SecurityManager.checkWrite(Ljava.lang.String;)V",
//                "java.lang.ProcessImpl.start",
//                "java.lang.ProcessImpl.createCommandLine",
//                "com.mysql.cj.jdbc.EscapeProcessor.escapeSQL",
//                "java.io.FileInputStream.<init>(Ljava.io.File;)V",
//                "java.io.FileOutputStream.<init>(Ljava.io.File;Z)V",
//                "java.io.ObjectInputStream.resolveClass(Ljava.io.ObjectStreamClass;)Ljava.lang.Class;",

//                "java.io.File.renameTo",
//                "java.io.File.list",
//                "org.springframework.web.servlet.DispatcherServlet.doService(Ljavax.servlet.http.HttpServletRequest;Ljavax.servlet.http.HttpServletResponse;)V",
//                "org.springframework.web.servlet.DispatcherServlet",
//                "org.apache.catalina.core.StandardEngineValve.invoke",
//                "javax.servlet.http.HttpServlet.service",
      "java.lang.Runtime.exec",
            "java.util.ArrayList.add"
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
      if (src.startsWith(clazz)) {
        return true;
      }
    }
    return false;
  }

  public static boolean isIncludedMethod(String src) {
    for (String method : include_methods) {
      if (src.startsWith(method)) {
        return true;
      }
    }
    return false;
  }
}
