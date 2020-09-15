package SecAgent.utils.DefaultLoggerHelper;

import java.io.IOException;
import java.util.logging.*;

public class DefaultLogger extends Logger {
  private static String DefaultLoggerName = "SecAgent";
  private static String LoggerName = null;
  private Handler handler = null;

  public DefaultLogger() throws IOException {
    super(DefaultLoggerName, null);
    handler = new DefaultLogFileHandler();
    this.addDefaultHandle();
  }

  public DefaultLogger(String log_path) throws IOException {
    super(DefaultLoggerName, null);
    handler = new DefaultLogFileHandler(log_path);
    this.addDefaultHandle();
  }

  protected DefaultLogger(String name, String resourceBundleName) throws IOException {
    super(name, resourceBundleName);
    this.LoggerName = name;
    handler = new DefaultLogFileHandler();
    this.addDefaultHandle();
  }

  private void addDefaultHandle() {
    this.addHandler(this.handler);
  }

  public void setFormatter(Formatter formatter) throws IOException {
    DefaultLogFileHandler handler = new DefaultLogFileHandler();
    if (handler == null) return;
    this.removeHandler(this.handler);
    this.handler = handler;
    handler.setFormatter(formatter);
    this.addHandler(handler);
  }

  public static DefaultLogger getInstance(String name) {
    DefaultLogger logger = DefaultLogger.getLogger(name);
    logger.addDefaultHandle();
    return logger;
  }

  public static DefaultLogger getInstance(Class cls) {
    return getInstance(cls.getName());
  }

  public static DefaultLogger getLogger() {
    DefaultLogger logger = null;
    try{
      logger = new DefaultLogger();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return logger;
  }

  public static DefaultLogger getLogger(String name) {
    DefaultLogger logger = null;
    try{
      logger = new DefaultLogger(name, null);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return logger;

  }

  public static DefaultLogger getLogger(Class cls) {
    return getLogger(cls.getName());
  }

  public void log(Level level, String msg) {
    LogRecord lr = new LogRecord(level, msg);
    lr.setLoggerName(LoggerName == null || LoggerName.isEmpty()? DefaultLoggerName: LoggerName);
    System.out.println(lr.getLoggerName());
    log(lr);
  }

  public void info(String msg) {
    log(Level.INFO, msg);
  }

  public void info(Object obj) {
    info(String.format("%s", obj));
  }

  public void info(int v) {
    info(String.format("%s", v));
  }

  public void info(boolean v) {
    info(String.format("%s", v));
  }

  public void info(char v) {
    info(String.format("%s", v));
  }

  public void info(double v) {
    info(String.format("%s", v));
  }

  public void info(byte v) {
    info(String.format("%s", v));
  }

  public void info(short v) {
    info(String.format("%s", v));
  }

  public void info(long v) {
    info(String.format("%s", v));
  }

  public void error(String msg) {
    log(DefaultLevel.ERROR, msg);
  }

  public void error(Object obj) {
//    logger.info(obj.toString());
//    super.info("");
    error(String.format("%s", obj));
  }

  public void error(int v) {
//    logger.info(String.format("%s", v));
    error(String.format("%s", v));
  }

  public void error(boolean v) {
    error(String.format("%s", v));
  }

  public void error(char v) {
    error(String.format("%s", v));
  }

  public void error(double v) {
    error(String.format("%s", v));
  }

  public void error(byte v) {
    error(String.format("%s", v));
  }

  public void error(short v) {
    error(String.format("%s", v));
  }

  public void error(long v) {
    error(String.format("%s", v));
  }


  public void debug(String msg) {
    log(DefaultLevel.DEBUG, msg);
  }


  public void debug(Object obj) {
//    logger.info(obj.toString());
//    super.info("");
    debug(String.format("%s", obj));
  }

  public void debug(int v) {
//    logger.info(String.format("%s", v));
    debug(String.format("%s", v));
  }

  public void debug(boolean v) {
    debug(String.format("%s", v));
  }

  public void debug(char v) {
    debug(String.format("%s", v));
  }

  public void debug(double v) {
    debug(String.format("%s", v));
  }

  public void debug(byte v) {
    debug(String.format("%s", v));
  }

  public void debug(short v) {
    debug(String.format("%s", v));
  }

  public void debug(long v) {
    debug(String.format("%s", v));
  }


  private static final class DefaultLevel extends Level {

    private static final String defaultBundle = "sun.util.logging.resources.logging";
    public static final Level ERROR = new DefaultLevel("ERROR",Integer.MAX_VALUE);
    public static final Level DEBUG = new DefaultLevel("DEBUG",Integer.MAX_VALUE);

    protected DefaultLevel(String name, int value) {
      super(name, value, defaultBundle);
    }

    protected DefaultLevel(String name, int value, String resourceBundleName) {
      super(name, value, resourceBundleName);
    }

  }
}
