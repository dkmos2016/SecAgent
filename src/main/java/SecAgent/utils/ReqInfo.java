package SecAgent.utils;

import java.util.HashMap;

public class ReqInfo {
  private String url;
  private String type;
  private HashMap<String, String> queries;
  private HashMap<String, String> headers;

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

  public HashMap<String, String> getQueries() {
    return queries;
  }

  public void setQueries(HashMap<String, String> queries) {
    this.queries = queries;
  }

  @Override
  public String toString() {
    return String.format("{\"url\":\"%s\",\"type\":\"%s\",\"queries\":\"%s\"}",
      url, type, queries);
  }
}
