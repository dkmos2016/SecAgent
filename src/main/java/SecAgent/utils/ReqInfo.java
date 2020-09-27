package SecAgent.utils;

import SecAgent.Conf.Config;
import SecAgent.utils.DefaultLoggerHelper.DefaultLogger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReqInfo {
  /** for log information */
  private static final DefaultLogger logger =
      DefaultLogger.getLogger(ReqInfo.class, Config.INFORMATION_PATH);

  static {
    if (logger != null) logger.setLevel(DefaultLogger.MyLevel.INFO);
  }

  private int state_code = 0;

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
  private Map queries = new HashMap<>();
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

    if (method.equals("POST")) {
      this.setInputStream(request.getInputStream());
    }
//    this.queries = request.getParameterMap();
    this.queryString = request.getQueryString();
    this.state_code |= ReqInfoState.PUTTED_URI | ReqInfoState.PUTTED_QUERYSTRING | ReqInfoState.PUTTED_METHOD | ReqInfoState.PUTTED_INPUTSTREAM;

    this.ALLOWED_PUT_STUB = true;

  }

  public void setHttpServletResponse(HttpServletResponse response) throws IOException {

    this.response = response;
  }

  public void setInputStream(InputStream inputStream) throws IOException {
    if (inputStream.available() <= 0 ) return;

    this.inputStream = inputStream;
    byte [] b = new byte[1025];

//    this.inputStream.mark(Integer.MAX_VALUE);
    this.inputStream.read(b);

    System.out.println("result1:" + new String(b));
//    this.inputStream.reset();
  }

  /**
   * for all stub to invoke setting stack info and params
   *
   * @param type
   * @param throwable
   * @param obj
   */
  private void putStubData(String type, Throwable throwable, Object... obj) {
    if (logger != null) logger.debug(obj);

    if (this.ALLOWED_PUT_STUB == false) return;

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

    return String.format(
        "{\"url\":\"%s\",\"method\":\"%s\",\"queries\":\"%s\",\"StubData\": \"%s\"}",
        url, method, queries, StubDatas);
  }

  /** for SecAgent to do some other jobs */
  public void doJob() {
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
      StackTraceElement[] stackTraceElements = this.throwable.getStackTrace();
      StringBuilder sb = new StringBuilder();
      for (StackTraceElement element : stackTraceElements) {
        String className = element.getClassName();
        if (className.startsWith("java.")
            || className.startsWith("sun.")
            || className.startsWith("javax.")
            || className.startsWith("SecAgent.")
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
        return sb.toString();
      } else {
        return object.toString();
      }
    }
  }
}
