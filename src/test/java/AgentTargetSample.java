import SecAgent.utils.Common;
import SecAgent.utils.ReqInfo;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
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
//    System.out.println("main start");
//    ExceptionLogger.doTestAsync(new Exception("select id, value from test where id = 1"));
//    ExceptionLogger.doTestAsync(new Exception("select id, value from test where id = 2"));
//    ExceptionLogger.doTestAsync(new Exception("select id, value from test where id = 3"));
//    //    ExceptionLogger.doTestAsync(new Exception("error test4"));
//    //    ExceptionLogger.doTestAsync(new Exception("error test5"));
//    ExceptionLogger.Shutdown();
//    System.out.println("main done");
//    new AgentTargetSample().test1(true, new short[]{1,2}, (byte) 1, 1, 1, 1, 1.0, null);

//    HttpClientHelper.doGet("https://www.baidu.com");

    HashMap headers = new HashMap();
    headers.put("Content-Type", "application/json; charset=utf-8");

    HashMap body = new HashMap();
    body.put("name", "test");
    body.put("password", "123456");

    ArrayList arrayList = new ArrayList();
    arrayList.add(1);
    arrayList.add(2);
    arrayList.add(3);

    body.put("tetst", arrayList);

    body.put("tttt", new Object[] {"4", "5", 6});

    body.put("tttt1", true);


    System.out.println(Common.MapToJsonStr(body));


//    HttpClientHelper.doPost("http://127.0.0.1:8888", headers, body);
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
