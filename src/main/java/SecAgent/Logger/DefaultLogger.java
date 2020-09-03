package SecAgent.Logger;

//import org.apache.log4j.Level;
//import org.apache.log4j.Logger;

//import org.apache.logging.log4j.Logger;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Level;

import java.lang.reflect.Method;

public class DefaultLogger {
//  private final static Logger logger = LogManager.getLogger("Default");
  private final static String[] LoggerClassNames;
  private static Object logger;

  private static Method info;
  private static Method debug;
  private static Method warn;
  private static Method error;

  private static Class Logger;


  static {
    LoggerClassNames = new String[]{
      "org.apache.log4j.Logger",
      "org.apache.log4j.Logging.Logger"
    };
    init();
  }



  public static void init(){
    Method method = null;
    for (String LoggerClassName: LoggerClassNames){
      try {
        Logger = Class.forName(LoggerClassName);
        method = Logger.getMethod("getLogger", String.class);
        logger = method.invoke(null, "SecAgent");

        info = Logger.getMethod("info", Object.class);
        debug = Logger.getMethod("debug", Object.class);
        warn = Logger.getMethod("warn", Object.class);
        error = Logger.getMethod("error", Object.class);

        break;
      } catch (Exception e){
        continue;
      }
    }
  }

  public static void info(Object obj) {
    try{
      info.invoke(logger, obj);
    } catch (Exception e) {
      ;
    }
  }

  public static void debug(Object obj) {
    try{
      debug.invoke(logger, obj);
    } catch (Exception e) {
      ;
    }
  }

  public static void warn(Object obj) {
    try{
      warn.invoke(logger, obj);
    } catch (Exception e) {
      ;
    }
  }

  public static void error(Object obj) {
    try{
      error.invoke(logger, obj);
    } catch (Exception e) {
      ;
    }
  }

  static void test() {
    info("hello");
  }
}
