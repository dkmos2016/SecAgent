package SecAgent.utils.MyLoggerHelper;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class DefaultLogFileHandler extends FileHandler {
    private final static String LOG_PATH = "SecAgent-INFO.log";

    public DefaultLogFileHandler() throws IOException {
        super();
        System.out.println(LOG_PATH);
        setFormatter(new DefaultLogFormat());
        setOutputStream(new FileOutputStream(LOG_PATH));

    }

    public DefaultLogFileHandler(String log_path) throws IOException, SecurityException {
//        super(log_path);

        System.out.println(log_path);

        if (log_path == null || log_path.equals("")) {
            setOutputStream(new FileOutputStream(this.LOG_PATH));
        } else {
            setOutputStream(new FileOutputStream(log_path));
        }

        setFormatter(new SimpleFormatter());
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
