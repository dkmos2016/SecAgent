import SecAgent.Logger.ExceptionLogger;
import SecAgent.utils.AgentClassLoader;
import SecAgent.utils.ReqInfo;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;

public class AgentTargetSample<E> {
  private int id;
  private String name;
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

  public AgentTargetSample() {

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
    new AgentTargetSample().test1(true, new short[]{1,2}, (byte) 1, 1, 1, 1, 1.0, null);
  }

  public  void test1(boolean g, short a[], byte c, int i, long l, float f, double d, String []aa) {
    try {
      int t = 0;
      int aaa = 2;
      int bbbaaa = 2;

      Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (Exception e) {
      e.printStackTrace();

      try{
        Class cls = Class.forName("com.mysql.cj.jdbc.Driver");
        Method method = cls.getMethod("test", null);
        method.invoke(null, null);
      } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |InvocationTargetException e1) {
        e1.printStackTrace();
      }
    }
  }
}
