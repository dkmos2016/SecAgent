package SecAgent.SecAgent_Core.Logger;


import SecAgent.SecAgent_Core.Conf.Config;
import SecAgent.SecAgent_Core.utils.DefaultLoggerHelper.DefaultLogger;

public class ExceptionLogger {
  public static final DefaultLogger logger =
      DefaultLogger.getLogger(ExceptionLogger.class, Config.EXCEPTION_PATH);

  public static void doExpLog(Exception e) {
    //        e.printStackTrace();
    if (logger != null) logger.error(e);
    e.printStackTrace();

    //    MysqlLogger.execute("select id, name from test");
    // todo async
  }
}
