package SecAgent.Logger;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class DefaultLogger {
  private final static Logger logger;
  private final static Level level;


  static {
    logger = Logger.getLogger("DEFAULT");
    level = Level.INFO;
    setLevel(level);
  }

  private static void setLevel(Level level) {
    logger.setLevel(level);
  }

  public static void info(Object obj) {
    logger.info(obj);
  }

  public static void debug(Object obj) {
    logger.debug(obj);
  }

  public static void warn(Object obj) {
    logger.warn(obj);
  }

  public static void error(Object obj) {
    logger.error(obj);
  }

  static void test() {
    info("hello");
  }
}
