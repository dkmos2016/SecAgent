package SecAgent.utils.DefaultLoggerHelper;

import SecAgent.Conf.Config;
import SecAgent.utils.Resources;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/** Default file handle read filepath from config.properties */
public class DefaultLogFileHandler extends FileHandler {

  /**
   * construct DefaultLogFileHandler with DefaultLogFormatter
   *
   * @throws IOException
   */
  public DefaultLogFileHandler() throws IOException {
    super();

    setFormatter(new DefaultLogFormat());
    setOutputStream(new FileOutputStream(Config.INFORMATION_PATH, true));
  }

  public DefaultLogFileHandler(Formatter formatter) throws IOException, SecurityException {
    super();
    setFormatter(formatter);
  }

  public DefaultLogFileHandler(String log_path) throws IOException, SecurityException {
    super();

    if (log_path == null || log_path.equals("")) {
      setOutputStream(new FileOutputStream(Config.INFORMATION_PATH, true));
    } else {
      setOutputStream(new FileOutputStream(log_path, true));
    }

    setFormatter(new DefaultLogFormat());
  }

  @Override
  public synchronized void publish(LogRecord record) {
    super.publish(record);
  }

  @Override
  public synchronized void flush() {
    super.flush();
  }

  @Override
  public synchronized void close() throws SecurityException {
    super.close();
  }
}
