package SecAgent.utils;

import SecAgent.Logger.DefaultLogger;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReqInfo {
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

  private InputStream inputStream;

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

  /**
   * for HttpServletRequest to invoke setting url
   *
   * @param url
   */
  public void setUrl(String url) {
    this.url = url;
    if (url != null) this.ALLOWED_PUT_STUB = true;
  }

  /**
   * for HttpServletRequest to invoke setting method
   *
   * @param method
   */
  public void setMethod(String method) {
    this.method = method;
  }

  /**
   * for HttpServletRequest to invoke setting QueryString
   *
   * @param queryString
   */
  public void setQueryString(String queryString) {
    this.queryString = queryString;
  }

  /**
   * for HttpServletRequest to invoke setting Queries
   *
   * @param queries
   */
  public void setQueries(Map queries) {
    this.queries = queries;
  }

  /**
   * for XXE_STUB @Deprecated for HttpServletRequest to invoke setting Queries
   *
   * @param inputStream
   */
  public void setInputStream(InputStream inputStream) {
    this.inputStream = inputStream;
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
    System.out.println("toString: ");
    System.out.println(this.inputStream);
    return String.format(
        "{\"url\":\"%s\",\"method\":\"%s\",\"queries\":\"%s\",\"StubData\": \"%s\"}",
        url, method, null, StubDatas);
  }

  /** for SecAgent to do some other jobs */
  public void doJob() {
    System.out.println("doJob:  ");

    System.out.println("SQL: " + Resources.getProperty("SQL"));
    try {
      //      System.out.println(this.toString());

      if (DefaultLogger.isFoundLog4j()) {
        DefaultLogger.info(this.toString());
      } else {
        System.out.println(this.toString());
      }

    } catch (Exception e) {
      System.out.println("not ready now!");
    }
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
