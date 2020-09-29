package SecAgent.utils.DefaultLoggerHelper;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class DefaultLogFormat extends Formatter {
  private static final String format = "%s [%s] [%s] -- %s %s\n";
  private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSS");

  protected DefaultLogFormat() {
    super();
  }

  @Override
  public String format(LogRecord record) {

    String source;
    if (record.getSourceClassName() != null) {
      source = record.getSourceClassName();

      if (record.getSourceMethodName() != null) {
        source += " " + record.getSourceMethodName();
      }
    } else {
      source = record.getLoggerName();
    }

    String message = formatMessage(record);
    String throwable = "";
    if (record.getThrown() != null) {
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      pw.println();
      record.getThrown().printStackTrace(pw);
      pw.close();
      throwable = sw.toString();
    }

    return String.format(
        format,
        sdf.format(record.getMillis()),
        //        source,
        record.getLevel().getName(),
        record.getLoggerName(),
        message,
        throwable);
  }
}
