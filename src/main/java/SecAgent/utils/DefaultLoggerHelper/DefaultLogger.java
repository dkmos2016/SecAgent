package SecAgent.utils.DefaultLoggerHelper;

import java.io.IOException;
import java.util.logging.*;

public class DefaultLogger extends Logger {
  private final static String DEFAULT_LOGGER_NAME;
  private static String LoggerName = null;
  private static MyLevel DEFAULT_LEVEL;

  private Handler file_handler = null;
  private Handler console_handler = null;

  static {
    DEFAULT_LOGGER_NAME = "SecAgent";
    DEFAULT_LEVEL = MyLevel.INFO;
    LoggerName = null;
  }

  public enum MyLevel {
    DEBUG(0), INFO(1),  WARN(2), ERROR(3),;
    private int value;
    MyLevel(int value) {
      this.value = value;
    }

    public int getValue(){
      return this.value;
    }

    public Level getLevel(){
      switch (this) {
        case DEBUG:
          return DefaultLevel.DEBUG;

        case WARN:
          return DefaultLevel.WARNING;

        case ERROR:
          return DefaultLevel.ERROR;

        case INFO:
        default:
          return Level.INFO;
      }
    }
  }

  public DefaultLogger() throws IOException {
    super(DEFAULT_LOGGER_NAME, null);
    this.file_handler = new DefaultLogFileHandler();
    this.console_handler = new DefaultLogConsoleHandler();

    this.addDefaultHandle();
  }

  public DefaultLogger(String log_path) throws IOException {
    super(DEFAULT_LOGGER_NAME, null);
    file_handler = new DefaultLogFileHandler(log_path);
    console_handler = new DefaultLogConsoleHandler();
    this.addDefaultHandle();
  }

  protected DefaultLogger(String name, String resourceBundleName) throws IOException {
    super(name, resourceBundleName);
    this.LoggerName = name;
    file_handler = new DefaultLogFileHandler();
    console_handler = new DefaultLogConsoleHandler();
    this.addDefaultHandle();
  }

  private void addDefaultHandle() {
    this.addHandler(this.file_handler);
    this.addHandler(this.console_handler);
  }

  public void setFormatter(Formatter formatter) throws IOException {
    DefaultLogFileHandler file_handler = new DefaultLogFileHandler();
    DefaultLogConsoleHandler console_handler = new DefaultLogConsoleHandler();
    if (file_handler == null || console_handler == null) return;
    this.removeHandler(this.file_handler);
    this.removeHandler(this.console_handler);
    this.file_handler = file_handler;
    this.console_handler = console_handler;

    this.file_handler.setFormatter(formatter);
    this.console_handler.setFormatter(formatter);
    this.addHandler(file_handler);
    this.addHandler(console_handler);
  }

  public static void setLevel(MyLevel mylevel) {
    DEFAULT_LEVEL = mylevel;
  }

  public MyLevel getLevel(MyLevel mylevel) {
    return DEFAULT_LEVEL;
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
    lr.setLoggerName(LoggerName == null || LoggerName.isEmpty()? DEFAULT_LOGGER_NAME : LoggerName);
    System.out.println(lr.getLoggerName());
    log(lr);
  }

  public void log(MyLevel level, String msg) {
    if (level.getValue() >= DEFAULT_LEVEL.getValue()) {
      log(level.getLevel(), msg);
    }
  }

  public void info(String msg) {
    log(MyLevel.INFO, msg);
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
    log(MyLevel.ERROR, msg);
  }

  public void error(Object obj) {
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
    log(MyLevel.DEBUG, msg);
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

  public void warn(String msg) {
    log(MyLevel.WARN, msg);
  }

  public void warn(Object obj) {
    warn(String.format("%s", obj));
  }

  public void warn(int v) {
    warn(String.format("%s", v));
  }

  public void warn(boolean v) {
    warn(String.format("%s", v));
  }

  public void warn(char v) {
    warn(String.format("%s", v));
  }

  public void warn(double v) {
    warn(String.format("%s", v));
  }

  public void warn(byte v) {
    warn(String.format("%s", v));
  }

  public void warn(short v) {
    warn(String.format("%s", v));
  }

  public void warn(long v) {
    warn(String.format("%s", v));
  }


  private static final class DefaultLevel extends Level {

    private static final String defaultBundle = "sun.util.logging.resources.logging";
    public static final Level ERROR;
    public static final Level DEBUG;
    public static final Level WARN;
    public static final Level INFO;

    static {
      ERROR = new DefaultLevel("ERROR", Integer.MAX_VALUE);
      DEBUG = new DefaultLevel("DEBUG", 0);
      WARN = Level.WARNING;
      INFO = Level.INFO;
    }

    protected DefaultLevel(String name, int value) {
      super(name, value, defaultBundle);
    }

    protected DefaultLevel(String name, int value, String resourceBundleName) {
      super(name, value, resourceBundleName);
    }
  }
}
