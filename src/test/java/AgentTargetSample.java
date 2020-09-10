import SecAgent.Logger.ExceptionLogger;
import SecAgent.utils.AgentClassLoader;
import SecAgent.utils.ReqInfo;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class AgentTargetSample<E> {
  private final int id;
  private final String name;
  private HttpServletRequest httpServletRequest;

  public AgentTargetSample(int id, String name) {
    StringBuilder sb = new StringBuilder();
    sb.append(id);
    sb.append("|");
    sb.append(name);
    sb.toString();
    this.id = id;
    this.name = name;
  }

  public static void test(Class[] params) {
    ReqInfo res;
    try {
      res =
          (ReqInfo)
              Thread.currentThread()
                  .getContextClassLoader()
                  .loadClass("SecAgent.utils.ReqInfo")
                  .getDeclaredMethod("doTest", params)
                  .invoke(null, 1, 2);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    System.out.println("main start");
    ExceptionLogger.doTestAsync(new Exception("select id, value from test where id = 1"));
    ExceptionLogger.doTestAsync(new Exception("select id, value from test where id = 2"));
    ExceptionLogger.doTestAsync(new Exception("select id, value from test where id = 3"));
    //    ExceptionLogger.doTestAsync(new Exception("error test4"));
    //    ExceptionLogger.doTestAsync(new Exception("error test5"));
    ExceptionLogger.Shutdown();
    System.out.println("main done");
    test1(null);
  }

  public static void test1(HttpServletRequest request) {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
