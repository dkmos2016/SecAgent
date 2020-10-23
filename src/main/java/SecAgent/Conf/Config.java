package SecAgent.Conf;

import SecAgent.utils.DefaultLoggerHelper.DefaultLogger;
import SecAgent.utils.Resources;

import java.io.File;

public class Config {
  /** allow to show log to console */
  public static final boolean CONSOLE_DEBUG;

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


  public static int state_code = 0;

  static {
    CONSOLE_DEBUG = false;
    ALLOWED_DIY_LEVEL = false;
//    CONSOLE_DEBUG =


    DEFAULT_LOGGER_NAME = "SecAgent";
    DEFAULT_LOGGER_LEVEL = DefaultLogger.MyLevel.INFO;

    DEBUG_PATH = Resources.getProperty("DEBUG_LOG_PATH");
    INFORMATION_PATH = Resources.getProperty("LOG_DIR") + File .separator + Resources.getProperty("INFORMATION_LOG_PATH");
    EXCEPTION_PATH = Resources.getProperty("LOG_DIR") + File .separator + Resources.getProperty("EXCEPTION_LOG_PATH");
  }
}
