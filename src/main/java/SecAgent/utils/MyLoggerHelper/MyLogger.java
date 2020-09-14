package SecAgent.utils.MyLoggerHelper;

import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class MyLogger extends Logger {
  private static final Logger logger = Logger.getLogger("SecAgent");

  static {
    System.out.println(logger);
    addDefaultHandle(logger);
  }

  protected MyLogger(String name, String resourceBundleName) {
    super(name, resourceBundleName);
  }

  private static void addDefaultHandle(Logger _logger) {
    try {
      if (_logger == null) {
        System.out.println("cannot get logger");
      }
      _logger.addHandler(new DefaultLogFileHandler());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static Logger getInstance(String name) {
    Logger _logger = LogManager.getLogManager().getLogger(name);
    addDefaultHandle(_logger);

    return _logger;
  }

  public static Logger getInstance(Class cls) {
    Logger _logger = LogManager.getLogManager().getLogger(cls.getName());
    addDefaultHandle(_logger);

    return _logger;
  }

  public static Logger getLogger() {
    return logger;
  }
}
