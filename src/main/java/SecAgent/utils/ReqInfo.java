package SecAgent.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ReqInfo {
  private HttpServletRequest httpServletRequest;

  /**
   * complete url
   */
  private String url;

  /**
   * http method, GET/POST...
   */
  private String method;

  /**
   * stub type, exec/sql/...
   */
  private String type;

  /**
   * stub data
   */
  private String StubData;
  private Object[] StubDatas;

  /**
   * request parameters (include url & body)
   */
  private Map<String, String[]> queries = new HashMap<>();

  /**
   * reversed
   */
  private Map<String, String> headers = new HashMap<>();

  public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
    this.httpServletRequest = httpServletRequest;
    this.url = String.format("%s://%s:%d%s", httpServletRequest.getScheme(),
      httpServletRequest.getServerName(), httpServletRequest.getServerPort(), httpServletRequest.getRequestURI());
    this.method = httpServletRequest.getMethod();

    this.queries = httpServletRequest.getParameterMap();
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public String getStubData() {
    return StubData;
  }

  public void setStubData(String stubData) {
    StubData = stubData;
  }

  public String getStubDatas(String sep) {
    StringBuilder sb = new StringBuilder();
    for (Object obj : this.StubDatas) {
      sb.append(obj);
      sb.append(sep);
    }
    return sb.toString();
  }

  public void setStubDatas(Object[] stubDatas) {
    StubDatas = stubDatas;
  }

  public String getQueries() {
    StringBuilder result = new StringBuilder();
    String key;
    String[] values;
    result.append("{");
    for (Iterator<Map.Entry<String, String[]>> it = queries.entrySet().iterator(); it.hasNext(); ) {
      Map.Entry<String, String[]> entry = it.next();
      key = entry.getKey();
      values = entry.getValue();
      result.append(String.format("\"%s\":\"%s\",", key, values[0]));
    }
    result.append("}");
    return result.toString();
  }

  public void setQueries(Map<String, String[]> queries) {
    this.queries = queries;
  }

  public String getHeaders() {
    StringBuilder result = new StringBuilder();
    String key;
    String value;
    result.append("{");
    for (Iterator<Map.Entry<String, String>> it = headers.entrySet().iterator(); it.hasNext(); ) {
      Map.Entry<String, String> entry = it.next();
      key = entry.getKey();
      value = entry.getValue();
      result.append(String.format("\"%s\":\"%s\",", key, value));
    }
    result.append("}");
    return result.toString();
  }

  public void setHeaders(HashMap<String, String> headers) {
    this.headers = headers;
  }

  @Override
  public String toString() {
//    return String.format("{\"url\":\"%s\",\"method\":\"%s\",\"queries\":\"%s\", \"stubdata\": \"%s\"}",
//      url, method, null, this.getStubDatas(" "));

    return String.format("{\"url\":\"%s\",\"method\":\"%s\",\"queries\":\"%s\", \"type\": \"%s\",\"StubData\": \"%s\"}",
      url, method, null, type, StubData);
  }
}
