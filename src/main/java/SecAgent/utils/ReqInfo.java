package SecAgent.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class ReqInfo {
  private AgentClassLoader agentClassLoader;

  // store httpservletrequest
  private HttpServletRequest httpServletRequest;

  // store throwable & parameter
  private final Map<String, StubData> StubDatas = new HashMap<>();

  /**
   * complete url
   */
  private String url;

  /**
   * http method, GET/POST...
   */
  private String method;

  /**
   * request parameters (include url & body)
   */
  private Map<String, String[]> queries = new HashMap<>();

  /**
   * reversed
   */
  private final Map<String, String> headers = new HashMap<>();

  public ReqInfo(){
    System.out.println(this.getClass().getClassLoader());
  }

  public void setHttpServletRequest(HttpServletRequest httpServletRequest) {

    System.out.println("setHttpServletRequest: ");
    agentClassLoader = new AgentClassLoader(Thread.currentThread().getContextClassLoader());

    System.out.println(this.agentClassLoader.getParent());
    this.httpServletRequest = httpServletRequest;
    for (int i=0;i < 3; i++) {
      if (i == 0) {
        agentClassLoader = new AgentClassLoader(Thread.currentThread().getContextClassLoader());
      } else if (i == 1) {
        agentClassLoader = new AgentClassLoader(this.getClass().getClassLoader());
      } else {
        agentClassLoader = new AgentClassLoader(httpServletRequest.getClass().getClassLoader());
      }
      try {
        initExtra();
        break;
      } catch (Exception e) {
        e.printStackTrace();
        continue;
      }
    }

  }

  /**
   * generate other fields from httpServletRequest
   */
  public void initExtra() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Class cls = Class.forName("javax.servlet.http.HttpServletRequest", true, agentClassLoader);

    Class [] paraTypes = new Class[]{};
    Object [] paras = new Object[]{};

    String scheme;
    String host;
    int port;
    String uri;

    // setUrl
    Method method = cls.getMethod("getScheme", new Class[]{});

//      initExtra();

    method = cls.getMethod("getScheme", paraTypes);
    scheme = (String)method.invoke(httpServletRequest, paras);

    method = cls.getMethod("getServerName", paraTypes);
    host = (String)method.invoke(httpServletRequest, paras);

    method = cls.getMethod("getServerPort", paraTypes);
    port = (int)method.invoke(httpServletRequest, paras);

    method = cls.getMethod("getRequestURI", paraTypes);
    uri = (String) method.invoke(httpServletRequest, paras);

    method = cls.getMethod("getMethod", paraTypes);
    this.method = (String) method.invoke(httpServletRequest, paras);


    this.url = String.format("%s://%s:%d%s", scheme,
      host, port, uri);

    method = cls.getMethod("getParameterMap", paraTypes);
    this.queries = (Map)method.invoke(httpServletRequest, paras);

    System.out.println(this.method);
    System.out.println(this.url);
    System.out.println(this.queries);

    // todo
    // this.headers
  }

  public void putStubData(String type, Throwable throwable, Object obj) {
    try {
      Class cls = Class.forName("javax.servlet.http.HttpServletRequest", true, agentClassLoader);
      System.out.println("putstubdata: " + type);
      StubDatas.put(type, new StubData(throwable, obj));
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public String processStubData() {
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<String, StubData> entry: StubDatas.entrySet()) {
      String type = entry.getKey();
      StubData stubData = entry.getValue();
      sb.append(type +" " + stubData+"|+++|");
    }
    return sb.toString();
  }

  @Override
  public String toString() {
    return String.format("{\"url\":\"%s\",\"method\":\"%s\",\"queries\":\"%s\",\"StubData\": \"%s\"}",
      url, method, null, StubDatas);
  }

  public void doJob() {
    System.out.println("doJob:  ");
    System.out.println(this.toString());
  }

  static public void doTest(int a, int b) {
    System.out.println("doTest:  ");
    System.out.println(a + b);
  }

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
      if (object instanceof File) {
        return ((File) object).getPath();
      } else if (object instanceof Object[]) {
        StringBuilder sb = new StringBuilder();
        for (Object o: (Object[])object) {
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
