package SecAgent.Utils.Logger;

// import org.apache.log4j.Level;
// import org.apache.log4j.Logger;

// import org.apache.logging.log4j.Logger;

import SecAgent.Utils.utils.Pair;

import java.lang.reflect.Method;

@Deprecated
public class DefaultLogger {
  private static final Pair[] LoggerClassNames;

  private static boolean FOUND_LOG4J = false;
  private static Object logger;

  private static Method info;
  private static Method debug;
  private static Method warn;
  private static Method error;

  private static Class Logger;
  //  private static Field level;

  static {
    LoggerClassNames =
        new Pair[] {
          new Pair("org.slf4j.LoggerFactory", "org.slf4j.Logger"),
          new Pair("org.apache.log4j.Logger", "org.apache.log4j.Logger"),
          new Pair("org.apache.logging.log4j.LogManager", "org.apache.logging.log4j.Logger"),
        };
    init();
  }

  public static void init() {
    Method method = null;
    for (Pair pair : LoggerClassNames) {
      try {
        Logger = Thread.currentThread().getContextClassLoader().loadClass(pair.getKey());
        method = Logger.getMethod("getLogger", String.class);
        logger = method.invoke(null, "SecAgent/Utils");

        //        level = Class.forName("org.apache.logging.log4j.Level").getField("INFO");

        Logger = Thread.currentThread().getContextClassLoader().loadClass(pair.getValue());
        //        method = Logger.getMethod("setLevel", Level.class);
        //        method.invoke(logger, Level.INFO);

        info = Logger.getMethod("info", String.class);
        debug = Logger.getMethod("debug", String.class);
        warn = Logger.getMethod("warn", String.class);
        error = Logger.getMethod("error", String.class);

        FOUND_LOG4J = true;
        break;
      } catch (Exception e) {
        //        continue;
        e.printStackTrace();
      }
    }
  }

  public static void info(Object obj) {
    try {
      info.invoke(logger, obj);
    } catch (Exception e) {
    }
  }

  public static void debug(Object obj) {
    try {
      debug.invoke(logger, obj);
    } catch (Exception e) {
    }
  }

  public static void warn(Object obj) {
    try {
      warn.invoke(logger, obj);
    } catch (Exception e) {
    }
  }

  public static void error(Object obj) {
    try {
      error.invoke(logger, obj);
    } catch (Exception e) {
    }
  }

  static void test() {
    info("hello");
  }

  public static boolean isFoundLog4j() {
    return FOUND_LOG4J;
  }
}
