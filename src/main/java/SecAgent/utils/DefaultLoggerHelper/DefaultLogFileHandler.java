package SecAgent.utils.DefaultLoggerHelper;

import SecAgent.utils.Resources;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.LogRecord;
import java.util.logging.Formatter;

/**
 * Default file handle
 * read filepath from config.properties
 */
public class DefaultLogFileHandler extends FileHandler {
    private final static String LOG_PATH = Resources.getProperty("INFORMATION_LOG_PATH");

    /**
     * construct DefaultLogFileHandler with DefaultLogFormatter
     * @throws IOException
     */
    public DefaultLogFileHandler() throws IOException {
        super();
//        System.out.println(LOG_PATH);
        setFormatter(new DefaultLogFormat());
        setOutputStream(new FileOutputStream(LOG_PATH));
    }

    public DefaultLogFileHandler(Formatter formatter) throws IOException, SecurityException {
        super();
        setFormatter(formatter);
    }

    public DefaultLogFileHandler(String log_path) throws IOException, SecurityException {
        super();

        System.out.println("log_path: " + log_path);

        if (log_path == null || log_path.equals("")) {
            setOutputStream(new FileOutputStream(this.LOG_PATH));
        } else {
            setOutputStream(new FileOutputStream(log_path));
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
