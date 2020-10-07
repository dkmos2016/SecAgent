package SecAgent.Conf;

import SecAgent.utils.DefaultLoggerHelper.DefaultLogger;
import SecAgent.utils.Resources;

public class Config {
  public final static boolean ALLOWED_DIY_DEBUG;
  public static final String DEFAULT_LOGGER_NAME;
  public final static DefaultLogger.MyLevel DEFAULT_LOGGER_LEVEL;

  public static final String DEBUG_PATH;
  public static final String EXCEPTION_PATH;
  public static final String INFORMATION_PATH;


  static {
    ALLOWED_DIY_DEBUG = true;
    DEFAULT_LOGGER_NAME = "SecAgent";
    DEFAULT_LOGGER_LEVEL = DefaultLogger.MyLevel.INFO;

    DEBUG_PATH = Resources.getProperty("DEBUG_LOG_PATH");
    EXCEPTION_PATH = Resources.getProperty("INFORMATION_LOG_PATH");
    INFORMATION_PATH = Resources.getProperty("EXCEPTION_LOG_PATH");
  }
}
