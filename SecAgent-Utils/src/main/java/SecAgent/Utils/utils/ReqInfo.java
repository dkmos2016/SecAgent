package SecAgent.Utils.utils;

import SecAgent.Utils.Conf.Config;
import SecAgent.Utils.utils.DefaultLoggerHelper.DefaultLogger;
import SecAgent.Utils.utils.Encoder.Base64;

import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReqInfo {
  /**
   * for log information
   */
  private static final DefaultLogger logger =
    DefaultLogger.getLogger(ReqInfo.class, Config.INFORMATION_PATH);
  private static final Map<String, Method> methods = new HashMap();

  static {
    if (logger != null) logger.setLevel(DefaultLogger.MyLevel.DEBUG);
  }

  // store throwable & parameter
  private final Map<String, ArrayList<StubData>> StubDatas = new HashMap<>();
  /**
   * for logging mybatis(template, input parameter, sql string)
   */
  private final Map<String, ArrayList<String>> MYBATIS_CACHES = new HashMap<>();
//    private final Map<String, Map<String, ArrayList<String>>> MYBATIS_CACHES = new HashMap<>();
  /**
   * reversed
   */
  private final Map<String, String> headers = new HashMap<>();
  @Deprecated
  private final Map protocols = new HashMap();
  /**
   * request parameters (include url & body)
   */
  private Map queries = new HashMap<>();
  @Deprecated
  private int state_code = 0;
  private Protocol current_protocol = null;
  /**
   * complete url
   */
  private String url;
  /**
   * with url
   */
  private boolean ALLOWED_PUT_STUB = false;
  /**
   * http method, GET/POST...
   */
  private String method;
  /**
   * get queryString
   */
  private String queryString;
  /**
   * getInpusteram
   */
  private InputStream inputStream;
  private OutputStream outputStream = new ByteArrayOutputStream();
  /**
   * inputstream's content
   */
  private String inputBuffer = null;

  private String dubbo_url = null;
  private String dubbo_msg = null;


  public ReqInfo() {
  }

  /**
   * just for test
   *
   * @param a
   * @param b
   */
  public static void doTest(int a, int b) {
  }

  /**
   * find and save method with zero parameter. eg: System.currentMills()
   */
  private Method doGetOrFindMethod(Object obj, String methodname) {
    Method method = methods.getOrDefault(methodname, null);

    if (method == null) {
      try {
        method = obj.getClass().getMethod(methodname);
        methods.put(methodname, method);
      } catch (Exception e) {
//                System.out.println(obj.getClass().getName());
        logger.error(e);
        method = null;
      }
    }

    return method;
  }

  /**
   * printStack
   */

  private void printStack() {
    StringBuilder sb = new StringBuilder();
    for (StackTraceElement element : new Throwable().getStackTrace()) {
      sb.append(element.toString() + "\n");
    }
    logger.debug(sb.toString() + "\n");
  }

  public void setQueries(Map queries) {
    this.queries = queries;
  }

  public void setRequestInfo(Map map) {
//        System.out.println(String.format("[DEBUG] %d setRequestInfo: ", Thread.currentThread().getId()));

    this.url = (String) map.getOrDefault("url", "");
    this.method = (String) map.getOrDefault("method", "");
    this.queryString = (String) map.getOrDefault("queryString", "");

//        this.queryString = (String) map.getOrDefault("queries", "");

    this.ALLOWED_PUT_STUB = true;

    Protocol protocol = Protocol.HTTP;
    this.current_protocol = this.current_protocol == null ? protocol : (this.current_protocol.getTimestamp() > protocol.getTimestamp() ? protocol : this.current_protocol);
  }


  public void setDubboInfo(Map map) {
//        System.out.println(String.format("[DEBUG] %d setDubboInfo: ", Thread.currentThread().getId()));
    if(logger!=null) logger.debug("tid "+Thread.currentThread().getId()+", setDubboInfo: ");
    if (map == null || map.isEmpty()) return;
    this.dubbo_url = (String) map.getOrDefault("invoker", "");

//        System.out.println("[DEBUG] " + map);
//        System.out.println("[DEBUG] setDubboInfo done.");
    Object object;
    String tmp;

    //    this.dubbo_url = (String)map.getOrDefault("channel", "");
    object = map.getOrDefault("channel", "");
    tmp = object == null ? "" : object.toString();
    this.dubbo_url = tmp.substring(tmp.indexOf("/") + 1, tmp.lastIndexOf("=") - 1);

    //    this.dubbo_msg = (String)map.getOrDefault("message", "");
    object = map.getOrDefault("message", "");
    tmp = object == null ? "" : object.toString();
    tmp = tmp.substring(tmp.indexOf("[") + 1, tmp.lastIndexOf("]"));
    this.dubbo_msg = tmp.substring(tmp.indexOf("[") + 1, tmp.lastIndexOf("]"));

    this.ALLOWED_PUT_STUB = true;

    Protocol protocol = Protocol.DUBBO;
//        System.out.println("DUBBO timestamp: " + protocol.timestamp);
    this.current_protocol = this.current_protocol == null ? protocol : (this.current_protocol.getTimestamp() > protocol.getTimestamp() ? protocol : this.current_protocol);

    //  for DEBUG
//        System.out.println("[DEBUG] " + this.dubbo_url);
//        System.out.println("[DEBUG] " + this.dubbo_msg);
//
//        Iterator<Map.Entry> iterator = map.entrySet().iterator();
//
//        while (iterator.hasNext()) {
//            Map.Entry entry = iterator.next();
//            String key = (String) entry.getKey();
//            Object value = entry.getValue();
//            System.out.println(String.format("[DEBUG] %s: %s", key, value == null ? null : value.toString()));
//        }
//
//
//        printStack();
  }

  /**
   * for inoke to set Request, reversed
   *
   * @param request
   * @throws IOException
   */
  public void setHttpServletRequest(Object request) {
    if (request == null) return;

    logger.debug("setHttpServletRequest: ");
//        printStack();

    try {
      this.url = ""
        + doGetOrFindMethod(request, "getScheme").invoke(request)
        + "://"
        + doGetOrFindMethod(request, "getServerPort").invoke(request)
        + ":"
        + doGetOrFindMethod(request, "getServerPort").invoke(request)
        + doGetOrFindMethod(request, "getRequestURI").invoke(request);
      this.method = "" + doGetOrFindMethod(request, "getMethod").invoke(request);

      //    this.queries = request.getParameterMap();
      this.queryString = "" + doGetOrFindMethod(request, "getQueryString").invoke(request);

      this.state_code |=
        ReqInfoState.PUTTED_URI
          | ReqInfoState.PUTTED_QUERYSTRING
          | ReqInfoState.PUTTED_METHOD
          | ReqInfoState.PUTTED_INPUTSTREAM;

    } catch (Exception e) {
      logger.error(e);
    } finally {
      this.ALLOWED_PUT_STUB = true;

      Protocol protocol = Protocol.HTTP;
      this.current_protocol = this.current_protocol == null ? protocol : (this.current_protocol.getTimestamp() > protocol.getTimestamp() ? protocol : this.current_protocol);
    }
  }


  /**
   * for all stub to invoke setting stack info and params
   *
   * @param type
   * @param throwable
   * @param obj
   */
  public void putStubData(String type, Throwable throwable, Object obj) {
    if (logger != null) logger.debug("tid "+Thread.currentThread().getId()+", putStubData:" + obj);
//        printStack();

    if (!this.ALLOWED_PUT_STUB) return;

    ArrayList list = new ArrayList();

    switch (type.toLowerCase()) {
      case "mybatis":
        doPutMybatis(type, throwable, obj);
        break;

      case "xxe":
        list = doPutXxe(type, throwable, obj);
        break;

//            case "dubbo":
//                doPutDubbo(type, throwable, obj);
//                break;

      case "cmd":
      default:
        list = doPutCommon(type, throwable, obj);
        break;
    }

    if (list != null) StubDatas.put(type, list);
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
  private void doPutMybatis(String type, Throwable throwable, Object obj) {
    StringBuilder sb = new StringBuilder();
    if (obj instanceof ArrayList) {
      String name = ""+((ArrayList<?>) obj).get(0);
      String _type = ""+((ArrayList<?>) obj).get(1);
      String value = ""+((ArrayList<?>) obj).get(2);

      if(value == null || value.isEmpty()) return;

      ArrayList list = MYBATIS_CACHES.getOrDefault(name, new ArrayList());
      sb.append("\\\"");
      sb.append(value);
      sb.append("\\\"");

      value = sb.toString();

      switch (_type) {
        case "BEFORE":
          value = "B:" + value;
          if (list.size() > 0) {
            list.add(0, value);
          } else {
            list.add(value);
          }
          break;

        case "AFTER":
          value = "A:" + value;
          if (list.size() >= 1) {
            list.add(1, value);
          } else {
            list.add(value);
          }

          break;

        case "PARAMETER":
          value = "P:" + value;
          //          list.add(String.format(String.format("%s: %s", _type, value)));
          list.add(value);
          break;

        default:
          break;
      }

      logger.debug(list);

      MYBATIS_CACHES.put(name, list);
    }
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
    try {
      if (obj instanceof InputStream) {
        list.add(new StubData(throwable, obj));
      }
    } catch (Exception e) {
      logger.error(e);
    }

    return list;
  }

  @Deprecated
  private void doPutDubbo(String type, Throwable throwable, Object obj) {
    ArrayList list = StubDatas.getOrDefault(type, new ArrayList());
    try {
      if (obj instanceof ArrayList) {
//        list.add(new StubData(throwable, obj));
        System.out.println(obj);
      }
    } catch (Exception e) {
      logger.error(e);
    }
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

  private String getFormatedData(String key, Object value) {
//      String format = "\"%s\": \"%s\"";
    StringBuilder sb = new StringBuilder();
    sb.append("\"");
    sb.append(key);
    sb.append("\": ");

    sb.append("\"");
    sb.append(value);
    sb.append("\"");

    return sb.toString();
  }

  private String getLogRecord(String type, ArrayList datas) {
    String result = null;
    StringBuilder sb = new StringBuilder();
//        ArrayList<String> arrayList = new ArrayList();

    sb.append("{");

    if (this.url != null) {
      sb.append(getFormatedData("url", this.url));
      sb.append(", ");
      sb.append(getFormatedData("method", this.method));
      sb.append(", ");


      if (method.toUpperCase().equals("GET")) {
        sb.append(getFormatedData("queryString", this.queryString));
        sb.append(", ");
      } else if (method.toUpperCase().equals("POST")) {
        if (this.inputBuffer == null) {
          if (this.queries.isEmpty()) {
            int v = -1;
            String tmp = "";
            try {
              while ((v = this.inputStream.read()) != -1) {
                tmp += String.format("%c", v);
              }
            } catch (Exception e) {
              tmp = "";
            }
            this.inputBuffer = tmp;
            sb.append(getFormatedData("inputStream", new String(Base64.encode(tmp.getBytes()))));
            sb.append(", ");
          } else {
            sb.append(getFormatedData("queries", Common.MapToFormData(this.queries)));
            sb.append(", ");
          }
        } else {
          sb.append(getFormatedData("queries", Common.MapToFormData(this.queries)));
          sb.append(", ");
        }
      } else {
        sb.append(getFormatedData("inputStream", null));
        sb.append(", ");
      }
    }

    sb.append(getFormatedData("type", type));

    if (!(this.dubbo_url == null || this.dubbo_url.isEmpty() || this.dubbo_msg == null || this.dubbo_msg.isEmpty())) {
      sb.append(", ");
      sb.append(getFormatedData("dubbo_url", this.dubbo_url));
      sb.append(",");
      sb.append(getFormatedData("dubbo_msg", this.dubbo_msg));
    }

    if (this.ALLOWED_PUT_STUB) {
      sb.append(", ");
      sb.append(getFormatedData("data", new String(Base64.encode(datas.toString().getBytes()))));
//            sb.append(getFormatedData("data", datas));
    }

    sb.append("}");

    return sb.toString();
  }


  private void doLogRecords() {
    for (Map.Entry<String, ArrayList<StubData>> entry : StubDatas.entrySet()) {
      String type = entry.getKey();
      ArrayList<StubData> list = entry.getValue();
      if (type == null || type.equals("") || list == null || list.size() == 0) {
        continue;
      } else {
        logger.info(getLogRecord(type, list));
      }
    }

    if (!MYBATIS_CACHES.isEmpty()) {
      for (List list : MYBATIS_CACHES.values()) {
        int sz = list.size();
        if (sz <= 2) continue;

        ArrayList tmp_list = new ArrayList();

        tmp_list.add(list.get(0));
        tmp_list.add(list.get(1));


        for (int i = 2; i < sz; i++) {
          tmp_list.add(String.format("param%d:%s", i - 1, list.get(i)));
        }

        logger.info(getLogRecord("MYBATIS", tmp_list));
      }
    }
  }

  /**
   * for SecAgent to do some other jobs
   */
  public void doJob(String name) {
    if (logger != null) logger.debug("tid "+Thread.currentThread().getId()+", doJob: ");
//        printStack();

    if (this.current_protocol != null && this.current_protocol.getName().equals(name)) {
      doLogRecords();
    }
  }

  public void setInputStream(InputStream inputStream) {
    this.inputStream = inputStream;
  }

  public OutputStream getOutputStream() {
    return this.outputStream;
  }

  public enum Protocol {
    HTTP("HTTP", 1), DUBBO("DUBBO", 2);

    private final String name;
    private final int index;
    private final long timestamp;

    Protocol(String s, int i) {
      this.name = s;
      this.index = i;
      this.timestamp = System.currentTimeMillis();
    }

    public long getTimestamp() {
      return this.timestamp;
    }

    public String getName() {
      return this.name;
    }
  }

  /**
   * StubData: Stack info and Parameters
   */
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
        return sb.substring(0, sb.length() - 1);
      } else if (object instanceof InputStream) {
        return new String(((ByteArrayOutputStream) Common.transferTo((InputStream) object)).toByteArray());
      } else {
        return object.toString();
      }
    }
  }
}
