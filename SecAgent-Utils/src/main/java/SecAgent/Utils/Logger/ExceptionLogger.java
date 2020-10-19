package SecAgent.Utils.Logger;

import SecAgent.Utils.Conf.Config;
import SecAgent.Utils.utils.DefaultLoggerHelper.DefaultLogger;

public class ExceptionLogger {
  public static final SecAgent.Utils.utils.DefaultLoggerHelper.DefaultLogger logger =
      DefaultLogger.getLogger(ExceptionLogger.class, Config.EXCEPTION_PATH);

  public static void doExpLog(Exception e) {
    //        e.printStackTrace();
    if (logger != null) logger.error(e);
    e.printStackTrace();

    //    MysqlLogger.execute("select id, name from test");
    // todo async
  }
}
