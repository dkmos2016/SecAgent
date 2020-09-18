package SecAgent.Logger;

import SecAgent.utils.DefaultLoggerHelper.DefaultLogger;
import SecAgent.utils.Resources;

import java.io.IOException;


public class ExceptionLogger {
    public final static DefaultLogger logger;

    static {
        System.out.println("initilize ExceptionLogger");
        DefaultLogger _logger = null;
        try {
            _logger = new DefaultLogger(Resources.getProperty("EXECPTION_LOG_PATH"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            logger = _logger;
        }
    }

    public static void doExpLog(Exception e) {
        System.out.println("doExpLog: ");
//        e.printStackTrace();

        logger.error(e);
//    MysqlLogger.execute("select id, name from test");

        // todo async
    }
}
