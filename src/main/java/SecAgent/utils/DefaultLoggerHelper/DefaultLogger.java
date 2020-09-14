package SecAgent.utils.DefaultLoggerHelper;

import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Formatter;

public class DefaultLogger extends Logger {
  private static final Logger logger = Logger.getLogger("SecAgent");

  static {
//    System.out.println(logger);

    addDefaultHandle(logger);
  }

  protected DefaultLogger(String name, String resourceBundleName) {
    super(name, resourceBundleName);
  }

  private static void addDefaultHandle(Logger _logger) {
    try {
      if (_logger == null) {
//        System.out.println("cannot get logger");
        return;
      }
      _logger.addHandler(new DefaultLogFileHandler());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void setFormatter(Logger _logger, Formatter formatter) throws IOException {
    DefaultLogFileHandler handler = new DefaultLogFileHandler();
    handler.setFormatter(formatter);

    _logger.addHandler(handler);
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
