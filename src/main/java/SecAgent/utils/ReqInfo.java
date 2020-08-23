package SecAgent.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

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
      httpServletRequest.getServerName(), httpServletRequest.getServerPort(),httpServletRequest.getRequestURI());
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

  public void setMethod(String method) {
    this.method = method;
  }

  public String getMethod() {
    return method;
  }

  public void setStubData(String stubData) {
    StubData = stubData;
  }

  public String getStubData() {
    return StubData;
  }

//  public void putQuery(String key, String value) {
//    queries.put(key, value);
//  }
//
//  public String getQuery(String key) {
//    return queries.get(key);
//  }

  public void setQueries(Map<String, String[]> queries) {
    this.queries = queries;
  }

  public String getQueries() {
    StringBuilder result = new StringBuilder();
    String key;
    String values[];
    result.append("{");
    for (Iterator<Map.Entry<String, String[]>> it = queries.entrySet().iterator(); it.hasNext(); ) {
      Map.Entry<String, String[]> entry = it.next();
      key = entry.getKey();
      values = entry.getValue();
      result.append(String.format( "\"%s\":\"%s\",",key, values[0]));
    }
    result.append("}");
    return result.toString();
  }

  public void setHeaders(HashMap<String, String> headers) {
    this.headers = headers;
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
      result.append(String.format( "\"%s\":\"%s\",", key, value));
    }
    result.append("}");
    return result.toString();
  }

  @Override
  public String toString() {
    return String.format("{\"url\":\"%s\",\"type\":\"%s\",\"queries\":\"%s\"}",
      url, method, null);
  }
}
