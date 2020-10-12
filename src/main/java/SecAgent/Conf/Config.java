package SecAgent.Conf;

import SecAgent.utils.DefaultLoggerHelper.DefaultLogger;
import SecAgent.utils.Resources;

public class Config {
  /** allow to show log to console */
  public static final boolean DEBUG;

  /** allow to set logger's level by owner */
  public static final boolean ALLOWED_DIY_LEVEL;

  /** Default Logger Name */
  public static final String DEFAULT_LOGGER_NAME;

  /** Default Logger Level */
  public static final DefaultLogger.MyLevel DEFAULT_LOGGER_LEVEL;

  /** logger.debug redirect to file */
  public static final String DEBUG_PATH;

  /** logger.error redirect to file */
  public static final String EXCEPTION_PATH;

  /** logger.info redirect to file */
  public static final String INFORMATION_PATH;

  static {
    DEBUG = false;
    ALLOWED_DIY_LEVEL = false;

    DEFAULT_LOGGER_NAME = "SecAgent";
    DEFAULT_LOGGER_LEVEL = DefaultLogger.MyLevel.INFO;

    DEBUG_PATH = Resources.getProperty("DEBUG_LOG_PATH");
    INFORMATION_PATH =
        DEBUG
            ? Resources.getProperty("INFORMATION_LOG_PATH")
            : (Resources.getProperty("LOG_DIR") + Resources.getProperty("INFORMATION_LOG_PATH"));
    EXCEPTION_PATH =
        DEBUG
            ? Resources.getProperty("EXCEPTION_LOG_PATH")
            : (Resources.getProperty("LOG_DIR") + Resources.getProperty("EXCEPTION_LOG_PATH"));
  }
}
