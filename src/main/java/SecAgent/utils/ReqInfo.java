package SecAgent.utils;

import SecAgent.utils.DefaultLoggerHelper.DefaultLogger;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReqInfo {
  /** for log information */
  private static final DefaultLogger logger;

  static {
    DefaultLogger _logger = null;
    try {
      _logger = new DefaultLogger();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      logger = _logger;
    }
  }

  // store throwable & parameter
  private final Map<String, ArrayList<StubData>> StubDatas = new HashMap<>();
  /** reversed */
  private final Map<String, String> headers = new HashMap<>();
  /** complete url */
  private String url;
  /** with url */
  private boolean ALLOWED_PUT_STUB = false;
  /** http method, GET/POST... */
  private String method;
  /** get queryString */
  private String queryString;
  /** request parameters (include url & body) */
  private Map<String, String[]> queries = new HashMap<>();
  /** getInpusteram */
  private InputStream inputStream;
  /**
   * HttpServletRequest
   */
  private HttpServletRequest request;

  public ReqInfo() {
    System.out.println(this.getClass().getClassLoader());
  }

  /**
   * just for test
   *
   * @param a
   * @param b
   */
  public static void doTest(int a, int b) {
    System.out.println("doTest:  ");
    System.out.println(a + b);
  }

  /**
   * is initialed url
   *
   * @return
   */
  public boolean isALLOWED_PUT_STUB() {
    return ALLOWED_PUT_STUB;
  }

  public void setHttpServletRequest(HttpServletRequest request) throws IOException {
    System.out.println("setHttpServletRequest: ");
    System.out.println(request);
    this.request = request;

    this.url = request.getRequestURI();
    this.method = request.getMethod();
    this.queries = request.getParameterMap();
    this.queryString = request.getQueryString();
    this.inputStream = request.getInputStream();
  }

  /**
   * for all stub to invoke setting stack info and params
   *
   * @param type
   * @param throwable
   * @param obj
   */
  public void putStubData(String type, Throwable throwable, Object obj) {
    //    String realType;
    System.out.println("putStubData: ");
    if (!ALLOWED_PUT_STUB) {
      System.out.println(String.format("skipped %s because of not setting url.", type));
      System.out.println(obj);
      return;
    }

    System.out.println("putstubdata: " + type + ", throwable: " + throwable + ", obj");

    ArrayList list = StubDatas.getOrDefault(type, new ArrayList());
    list.add(new StubData(throwable, obj));

    // may be not useful
    //    realType = (type.equals("DOWN") || type.equals("UPLOAD")) &&
    // obj.toString().endsWith(".xml")? "XXE": type;

    StubDatas.put(type, list);
  }

  /**
   * process StubDatas to display/log
   *
   * @return
   */
  private String processStubData() {
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<String, ArrayList<StubData>> entry : StubDatas.entrySet()) {
      String type = entry.getKey();

      ArrayList stubDatas = entry.getValue();
      //      StubData stubData = entry.getValue();
      sb.append(type + " " + stubDatas + "|+++|");
    }
    return sb.toString();
  }

  @Override
  public String toString() {
    System.out.println("ReqInfo toString: ");

    System.out.println(this.inputStream);
    return String.format(
        "{\"url\":\"%s\",\"method\":\"%s\",\"queries\":\"%s\",\"StubData\": \"%s\"}",
        url, method, null, StubDatas);
  }

  /** for SecAgent to do some other jobs */
  public void doJob() {
    System.out.println("doJob:  ");
    if (logger != null) logger.info(this.toString());
  }

  /** StubData: Stack info and Parameters */
  protected class StubData {
    protected Throwable throwable;
    Object object;

    public StubData(Throwable throwable, Object obj) {
      this.throwable = throwable;
      this.object = obj;
    }

    public String getTraceStack() {
      StackTraceElement[] stackTraceElements = throwable.getStackTrace();
      StringBuilder sb = new StringBuilder();
      for (StackTraceElement element : stackTraceElements) {
        sb.append(element.toString() + "\n");
      }
      return sb.toString();
    }

    @Override
    public String toString() {
      System.out.println("thorwable: " + throwable);
      if (object instanceof File) {
        return ((File) object).getPath();
      } else if (object instanceof Object[]) {
        StringBuilder sb = new StringBuilder();
        for (Object o : (Object[]) object) {
          sb.append(o);
          sb.append(" ");
        }
        return sb.toString();
      } else {
        return object.toString();
      }
    }
  }
}
