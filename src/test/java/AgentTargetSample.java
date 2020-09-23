import SecAgent.utils.DefaultLoggerHelper.DefaultLogger;
import SecAgent.utils.ReqInfo;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;

public class AgentTargetSample<E> {
  public static String name = "test";
  public InputStream in;
  public HttpServletRequest httpServletRequest;

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

    //    Logger logger = Logger.getLogger("test");
    //    try {
    //      FileHandler handle = new FileHandler("test.txt");
    //      handle.setFormatter(new Formatter() {
    //        @Override
    //        public String format(LogRecord record) {
    //          return record.getMillis() + "-" + record.getLoggerName() + "-" +
    // record.getMessage();
    //        }
    //      });
    //      logger.addHandler(handle);
    //
    //      logger.info("hello");
    //
    //    } catch (IOException e) {
    //      e.printStackTrace();
    //    }

    DefaultLogger logger = DefaultLogger.getLogger();
    //    logger.info("hello world!");
    //
    logger.info("test");

    logger.debug("done");

    logger.error("error");

    logger.info("info 2");

    //    HttpClientHelper.doPost("http://127.0.0.1:8888", headers, body);

  }

  public static void test() {}

  public static boolean getInt() {
    return true;
  }

  public void setInputSteam(InputStream in) throws IOException {
    this.in = in;
  }

  public InputStream getInputStream() throws IOException {
    if (this.in == null) {
      return this.httpServletRequest.getInputStream();
    } else {
      return this.in;
    }
  }

  public void test(String obj, String obj2) {
    try {
      Class cls = Class.forName("AgentTargetSample");
      Method method = cls.getMethod("getInt", null);
      Object o = method.invoke(null, null);
      boolean b = (boolean) o;
      if (b == true) {
        System.out.println("done.");
      }
    } catch (Exception e) {

    }
  }
}
