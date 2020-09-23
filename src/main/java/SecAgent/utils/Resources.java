package SecAgent.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Resources {
  private static final Properties properties;

  static {
    properties = new Properties();
    InputStream in =
        Resources.class.getClassLoader().getResourceAsStream("config/config.properties");
    try {
      properties.load(in);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static String getProperty(String key) {
    return properties.getProperty(key);
  }
}
