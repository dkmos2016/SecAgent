package SecAgent.Logger;

import SecAgent.utils.HttpClientLoggerHelper.HttpLogger;
import SecAgent.utils.SqlLoggerHelper.MysqlLogger;

import java.sql.ResultSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class ExceptionLogger {

  public static final ExecutorService es = Executors.newFixedThreadPool(3);

  public static void doExpLog(Exception e) {
    System.out.println("doExpLog: ");
    //    e.printStackTrace();
    MysqlLogger.execute("select id, name from test");
    // todo async
  }

  /**
   * use ExecutorServices to execute
   *
   * @param e
   */
  public static void doTestAsync(Exception e) {
    //    Future<Void> future = es.submit(
    //    es.submit(
    //        () -> {
    //          System.out.println("Async start...");
    //          Thread.sleep(5000);
    //          System.out.println("Async done...");
    //          return null;
    //        });

    es.submit(
        new Callable() {
          @Override
          public Object call() throws Exception {
            System.out.println("Async start...");
            //        Thread.sleep(5000);
            ResultSet rs = MysqlLogger.execute(e.getMessage());

            while (rs != null && rs.next()) {
              int id = rs.getInt(1);
              String value = rs.getString(2);
              System.out.println(String.format("%d %s", id, value));
            }

            System.out.println("Async done...");
            return null;
          }
        });
  }

  /**
   * use FutureTask to execute
   *
   * @param e
   */
  public static void doExcLogAsync(Exception e) {
    FutureTask futureTask = new FutureTask(new HttpLogger(e), null);
    es.execute(futureTask);
  }

  public static void Shutdown() {
    es.shutdown();
  }
}
