import SecAgent.utils.Common;
import SecAgent.utils.DefaultLoggerHelper.DefaultLogger;
import SecAgent.utils.ReqInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class AgentTargetSample<E> {
  private int id;
  private String name;
  private HttpServletRequest httpServletRequest;


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

//    Logger logger = Logger.getLogger("test");
//    try {
//      FileHandler handle = new FileHandler("test.txt");
//      handle.setFormatter(new Formatter() {
//        @Override
//        public String format(LogRecord record) {
//          return record.getMillis() + "-" + record.getLoggerName() + "-" + record.getMessage();
//        }
//      });
//      logger.addHandler(handle);
//
//      logger.info("hello");
//
//    } catch (IOException e) {
//      e.printStackTrace();
//    }

    Logger logger = DefaultLogger.getLogger();
    logger.info("hello world!");

    logger.info("test");


//    HttpClientHelper.doPost("http://127.0.0.1:8888", headers, body);
  }
}
