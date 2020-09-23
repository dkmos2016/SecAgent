package SecAgent.utils.DefaultLoggerHelper;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class DefaultLogConsoleHandler extends ConsoleHandler {
  /**
   * construct DefaultLogConsoleHandler with DefaultLogFormatter
   *
   * @throws IOException
   */
  public DefaultLogConsoleHandler() {
    super();
    setFormatter(new DefaultLogFormat());
  }

  public DefaultLogConsoleHandler(Formatter formatter) throws IOException, SecurityException {
    super();
    setFormatter(formatter);
  }

  @Override
  public void publish(LogRecord record) {
    super.publish(record);
  }

  @Override
  public void close() {
    super.close();
  }
}
