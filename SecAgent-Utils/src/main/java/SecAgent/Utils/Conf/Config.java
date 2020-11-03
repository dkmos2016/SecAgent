package SecAgent.Utils.Conf;

import SecAgent.Utils.utils.DefaultLoggerHelper.DefaultLogger;
import SecAgent.Utils.utils.JarClassLoader;
import SecAgent.Utils.utils.Resources;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

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

  public static final String JAR_PATH;


  public static final JarClassLoader jarLoader;

  /**
   * name of containers' jar
   */
  public final static Map<String, String> CONTAINER_JAR_FILE_NAMEs = new HashMap();

  /**
   * path of containers' jar, auto insert when release Container's jar from SecAgent.jar
   */
  public static final Map<String, String> CONTAINER_JAR_PATHs = new HashMap<>();

  static {
    CONSOLE_DEBUG = true;
    ALLOWED_DIY_LEVEL = true;

    DEFAULT_LOGGER_NAME = "SecAgent/Utils";
    DEFAULT_LOGGER_LEVEL = DefaultLogger.MyLevel.INFO;

    DEBUG_PATH = Resources.getProperty("DEBUG_LOG_PATH");
    INFORMATION_PATH =
      CONSOLE_DEBUG
        ? Resources.getProperty("INFORMATION_LOG_PATH")
        : (Resources.getProperty("LOG_DIR") + Resources.getProperty("INFORMATION_LOG_PATH"));
    EXCEPTION_PATH =
      CONSOLE_DEBUG
        ? Resources.getProperty("EXCEPTION_LOG_PATH")
        : (Resources.getProperty("LOG_DIR") + Resources.getProperty("EXCEPTION_LOG_PATH"));

  }

  /**
   * for container's jar
   */
  static {

    CONTAINER_JAR_FILE_NAMEs.put("TOMCAT", "SecAgent-Tomcat-1.0.0-SNAPSHOT-BEAT.jar");
    CONTAINER_JAR_FILE_NAMEs.put("TOMCAT4PREVIEW", "SecAgent-Tomcat4Preview-1.0.0-SNAPSHOT-BEAT.jar");
    CONTAINER_JAR_FILE_NAMEs.put("DUBBO", "SecAgent-Dubbo-1.0.0-SNAPSHOT-BEAT.jar");
    CONTAINER_JAR_FILE_NAMEs.put("JBOSS", "SecAgent-JBOSS.jar");

    JAR_PATH = Config.class.getProtectionDomain().getCodeSource().getLocation().toString().replace("file:/", "");

    ClassLoader parent = ClassLoader.getSystemClassLoader();
    jarLoader = new JarClassLoader(parent);
    jarLoader.setBaseUrl(JAR_PATH + File.separator + "libs/container/");
  }
}
