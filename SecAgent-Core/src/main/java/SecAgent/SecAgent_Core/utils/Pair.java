package SecAgent.SecAgent_Core.utils;

public class Pair {
  String key;
  String value;

  public Pair(Object key, Object value) {
    this.key = "" + key;
    this.value = "" + value;
  }

  public Pair(String key, String value) {
    this.key = key;
    this.value = "" + value;
  }

  public String getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.format("%s=%s", this.key, this.value);
  }
}
