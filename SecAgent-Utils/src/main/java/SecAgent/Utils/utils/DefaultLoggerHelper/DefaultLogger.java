package SecAgent.Utils.utils.DefaultLoggerHelper;

import SecAgent.Utils.Conf.Config;

import java.io.IOException;
import java.util.logging.*;

public class DefaultLogger extends Logger {
  private static final String DEFAULT_LOGGER_NAME;
  private static final MyLevel DEFAULT_LEVEL;

  static {
    DEFAULT_LOGGER_NAME =
            Config.DEFAULT_LOGGER_NAME.isEmpty() ? "DEFAULT" : Config.DEFAULT_LOGGER_NAME;
    DEFAULT_LEVEL =
            Config.DEFAULT_LOGGER_LEVEL == null ? MyLevel.INFO : Config.DEFAULT_LOGGER_LEVEL;
    //    DEFAULT_LEVEL = MyLevel.INFO;
  }

  private String LOGGER_NAME = null;
  private MyLevel LEVEL = null;
  private Handler file_handler = null;
  private Handler console_handler = null;

  public DefaultLogger() throws IOException {
    super(DEFAULT_LOGGER_NAME, null);
    this.file_handler = new DefaultLogFileHandler();
    this.console_handler = new DefaultLogConsoleHandler();

    this.addDefaultHandle();
  }

  public DefaultLogger(String name) throws IOException {
    this(name, null, null);
  }

  public DefaultLogger(String name, String log_path) throws IOException {
    this(name, log_path, null);
  }

  protected DefaultLogger(String name, String log_path, String resourceBundleName)
          throws IOException {
    super(name, resourceBundleName);
    this.LOGGER_NAME = name;
    file_handler = new DefaultLogFileHandler(log_path);
    console_handler = new DefaultLogConsoleHandler();
    this.addDefaultHandle();
  }

  public DefaultLogger(Class cls) throws IOException {
    this(cls.getName());
  }

  public DefaultLogger(Class cls, String log_path) throws IOException {
    this(cls.getName(), log_path);
  }

  protected DefaultLogger(Class cls, String log_path, String resourceBundleName)
          throws IOException {
    this(cls.getName(), log_path, resourceBundleName);
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
    try {
      logger = new DefaultLogger();
    } catch (IOException e) {
//      System.out.println("getLogger(): ");
      e.printStackTrace();
    }
    return logger;
  }

  public static DefaultLogger getLogger(String name) {
    DefaultLogger logger = null;
    try {
      logger = new DefaultLogger(name, null);
    } catch (IOException e) {
//      System.out.println("getLogger(String name): ");
      e.printStackTrace();
    }
    return logger;
  }

  public static DefaultLogger getLogger(String name, String log_path) {
    DefaultLogger logger = null;
    try {
      logger = new DefaultLogger(name, log_path);
    } catch (IOException e) {
//      System.out.println("getLogger(String name, String log_path): ");
      e.printStackTrace();
      logger = null;
    }

    return logger;
  }

  public static DefaultLogger getLogger(Class cls) {
    return getLogger(cls.getName());
  }

  public static DefaultLogger getLogger(Class cls, String log_path) {
    return getLogger(cls.getName(), log_path);
  }

  private void addDefaultHandle() {
    if (this.file_handler != null) this.addHandler(this.file_handler);

    if (this.console_handler != null && Config.CONSOLE_DEBUG) this.addHandler(this.console_handler);
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

  public Level getLevel() {
    return this.LEVEL == null ? DEFAULT_LEVEL.getLevel() : this.LEVEL.getLevel();
  }

  public void setLevel(MyLevel mylevel) {
    if (Config.ALLOWED_DIY_LEVEL) this.LEVEL = mylevel;
  }

  public void log(Level level, String msg) {
    LogRecord lr = new LogRecord(level, msg);
    lr.setLoggerName(
            LOGGER_NAME == null || LOGGER_NAME.isEmpty() ? DEFAULT_LOGGER_NAME : LOGGER_NAME);

    log(lr);
  }

  public void log(MyLevel level, String msg) {
    if (this.LEVEL == null) {
      if (level.getValue() >= DEFAULT_LEVEL.getValue()) {
        log(level.getLevel(), msg);
      }
    } else {
      if (level.getValue() >= this.LEVEL.getValue()) {
        log(level.getLevel(), msg);
      }
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

  public enum MyLevel {
    DEBUG(0),
    INFO(1),
    WARN(2),
    ERROR(3),
    ;
    private final int value;

    MyLevel(int value) {
      this.value = value;
    }

    public int getValue() {
      return this.value;
    }

    public Level getLevel() {
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

  private static final class DefaultLevel extends Level {

    public static final Level ERROR;
    public static final Level DEBUG;
    public static final Level WARN;
    public static final Level INFO;
    private static final String defaultBundle = "sun.util.logging.resources.logging";

    static {
      ERROR = new DefaultLevel("ERROR", Integer.MAX_VALUE);
      DEBUG = new DefaultLevel("DEBUG", Integer.MAX_VALUE);
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
