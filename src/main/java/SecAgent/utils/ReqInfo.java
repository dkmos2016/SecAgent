package SecAgent.utils;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ReqInfo {
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

  public void setUrl(String url) {
    this.url = url;
  }

  public void putStubData(String type, Throwable throwable, Object obj) {
      System.out.println("putstubdata: " + type + ", throwable: " + throwable + ", obj");
      StubDatas.put(type, new StubData(throwable, obj));
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
