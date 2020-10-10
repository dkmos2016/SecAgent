package SecAgent.Conf;

import SecAgent.utils.DefaultLoggerHelper.DefaultLogger;
import SecAgent.utils.Resources;

public class Config {
  public final static boolean DEBUG;
  public final static boolean ALLOWED_DIY_LEVEL;
  public static final String DEFAULT_LOGGER_NAME;
  public final static DefaultLogger.MyLevel DEFAULT_LOGGER_LEVEL;

  public static final String DEBUG_PATH;
  public static final String EXCEPTION_PATH;
  public static final String INFORMATION_PATH;


  static {
    DEBUG = false;
    ALLOWED_DIY_LEVEL = false;

    DEFAULT_LOGGER_NAME = "SecAgent";
    DEFAULT_LOGGER_LEVEL = DefaultLogger.MyLevel.INFO;

    DEBUG_PATH = Resources.getProperty("DEBUG_LOG_PATH");
    EXCEPTION_PATH = DEBUG? Resources.getProperty("INFORMATION_LOG_PATH"): (Resources.getProperty("LOG_DIR") + Resources.getProperty("INFORMATION_LOG_PATH"));
    INFORMATION_PATH = DEBUG? Resources.getProperty("INFORMATION_LOG_PATH"): Resources.getProperty("LOG_DIR") + Resources.getProperty("EXCEPTION_LOG_PATH");
  }
}
