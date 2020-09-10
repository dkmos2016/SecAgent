import SecAgent.Logger.ExceptionLogger;
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
    ExceptionLogger.doTestAsync(new Exception("select user from user"));
    ExceptionLogger.doTestAsync(new Exception("select host from user"));
    ExceptionLogger.doTestAsync(new Exception("select user, host from user"));
    //    ExceptionLogger.doTestAsync(new Exception("error test4"));
    //    ExceptionLogger.doTestAsync(new Exception("error test5"));
    ExceptionLogger.Shutdown();
    System.out.println("main done");
  }

  public void test(HttpServletRequest request) {
    try {
      System.out.println(request.getInputStream());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
