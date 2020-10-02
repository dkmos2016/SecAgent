package SecAgent.utils;

import SecAgent.Conf.Config;
import SecAgent.utils.DefaultLoggerHelper.DefaultLogger;
import SecAgent.utils.Encoder.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

public class ReqInfo {
  /** for log information */
  private static final DefaultLogger logger =
      DefaultLogger.getLogger(ReqInfo.class, Config.INFORMATION_PATH);

  static {
    if (logger != null) logger.setLevel(DefaultLogger.MyLevel.DEBUG);
  }

  // store throwable & parameter
  private final Map<String, ArrayList<StubData>> StubDatas = new HashMap<>();
  /** reversed */
  private final Map<String, String> headers = new HashMap<>();
  /** request parameters (include url & body) */
  private final Map queries = new HashMap<>();
  private int state_code = 0;
  /** complete url */
  private String url;
  /** with url */
  private boolean ALLOWED_PUT_STUB = false;
  /** http method, GET/POST... */
  private String method;
  /** get queryString */
  private String queryString;
  /** getInpusteram */
  private InputStream inputStream;
  /** HttpServletRequest */
  private HttpServletRequest request;
  /** HttpServletRequest */
  private HttpServletResponse response;

  public ReqInfo() {}

  /**
   * just for test
   *
   * @param a
   * @param b
   */
  public static void doTest(int a, int b) {}

  /**
   * is initialed url
   *
   * @return
   */
  public boolean isALLOWED_PUT_STUB() {
    return this.ALLOWED_PUT_STUB;
  }

  public void setHttpServletRequest(HttpServletRequest request) throws IOException {
    if (request == null) return;
    this.request = request;

    this.url =
        request.getScheme()
            + "://"
            + request.getServerName()
            + ":"
            + request.getServerPort()
            + request.getRequestURI();
    this.method = request.getMethod();

    //    this.queries = request.getParameterMap();
    this.queryString = request.getQueryString();
    this.state_code |=
        ReqInfoState.PUTTED_URI
            | ReqInfoState.PUTTED_QUERYSTRING
            | ReqInfoState.PUTTED_METHOD
            | ReqInfoState.PUTTED_INPUTSTREAM;

    this.ALLOWED_PUT_STUB = true;
  }

  public void setHttpServletResponse(HttpServletResponse response) throws IOException {
    this.response = response;
  }

  public void setInputStream(InputStream inputStream) throws IOException {
    if (inputStream.available() <= 0) return;
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
    if (logger != null) logger.debug(obj);
    if (!this.ALLOWED_PUT_STUB) return;

    ArrayList list = null;

    switch (type.toLowerCase()) {
      case "mybatis":
        list = doPutMybatis(type, throwable, obj);
        break;

      case "xxe":
        list = doPutXxe(type, throwable, obj);
        break;

      default:
        list = doPutCommon(type, throwable, obj);
        break;
    }

    StubDatas.put(type, list);
  }

  private ArrayList doPutCommon(String type, Throwable throwable, Object obj) {
    ArrayList list = StubDatas.getOrDefault(type, new ArrayList());
    list.add(new StubData(throwable, obj));
    return list;
  }

  /**
   * todo: mybatis
   *
   * @param type
   * @param throwable
   * @param obj
   * @return
   */
  private ArrayList doPutMybatis(String type, Throwable throwable, Object obj) {
    ArrayList list = StubDatas.getOrDefault(type, new ArrayList());
    List list1 = new ArrayList();
    if (obj instanceof  ArrayList) {
      String sql = ((List) obj).get(0).toString();

      list1.add(sql);
      list1.add(((List) obj).get(1));
    }

    list.add(new StubData(throwable, list1));

    return list;
  }

  /**
   * todo: mybatis
   *
   * @param type
   * @param throwable
   * @param obj
   * @return
   */
  private ArrayList doPutXxe(String type, Throwable throwable, Object obj) {
    ArrayList list = StubDatas.getOrDefault(type, new ArrayList());
    try{
      if (obj instanceof InputStream) {
        list.add(new StubData(throwable, obj));
      }
    } catch (Exception e) {
      logger.error(e);
    }

    return list;
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
    return String.format(
        "{\"url\":\"%s\",\"method\":\"%s\",\"queries\":\"%s\",\"StubData\": \"%s\"}",
        url, method, queries, StubDatas);
  }

  private String getLogRecord(String type, ArrayList<StubData> datas) {
    String result = null;

    if (method.toUpperCase().equals("GET")) {
      result = String.format(
              "{\"url\": \"%s\", \"method\": \"%s\", \"queryString\":\"%s\", \"type\": \"%s\", \"data\": \"%s\"}",
              this.url, this.method, this.queryString, type, datas);
    } else if (method.toUpperCase().equals("POST")) {
      result = String.format(
              "{\"url\": \"%s\", \"method\": \"%s\", \"queries\":\"%s\", \"type\": \"%s\", \"data\": \"%s\"}",
              this.url, this.method, this.queries, type, datas);
    } else {
      result = String.format(
          "{\"url\": \"%s\", \"method\": \"%s\", \"type\": \"%s\", \"data\": \"%s\"}",
          this.url, this.method, type, datas);
    }
    return result;
  }

  private void doLogRecords() {
    for(Map.Entry<String, ArrayList<StubData>> entry: StubDatas.entrySet()) {
      String type = entry.getKey();
      ArrayList list = entry.getValue();
      if (type == null || type.equals("") || list == null || list.size() == 0) {
        continue;
      } else {
        logger.info(getLogRecord(type, list));
      }
    }
  }

  /** for SecAgent to do some other jobs */
  public void doJob() {
    if (logger != null) doLogRecords();
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
      StackTraceElement[] stackTraceElements = this.throwable.getStackTrace();
      StringBuilder sb = new StringBuilder();
      for (StackTraceElement element : stackTraceElements) {
        String className = element.getClassName();
        if (className.startsWith("java.")
            || className.startsWith("sun.")
            || className.startsWith("javax.")
            || className.startsWith("SecAgent.")
            || className.startsWith("org.eclipse.")
            || className.startsWith("org.junit")
            || className.startsWith("org.apache.")) continue;
        sb.append(element.toString() + "\n");
      }
      return sb.toString();
    }

    @Override
    public String toString() {
      if (object instanceof File) {
        return ((File) object).getPath();
      } else if (object instanceof Object[]) {
        StringBuilder sb = new StringBuilder();
        for (Object o : (Object[]) object) {
          sb.append(o);
          sb.append(" ");
        }
        return sb.substring(0, sb.length()-1);
      } else if (object instanceof InputStream) {
        return new String(Base64.encode(((ByteArrayOutputStream)Common.transferTo((InputStream) object)).toByteArray()));
      }
      else {
        return object.toString();
      }
    }
  }
}
