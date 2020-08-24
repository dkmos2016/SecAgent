import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class AgentTargetSample {
  private final int id;
  private final String name;
  private HttpServletRequest httpServletRequest;

  public AgentTargetSample(int id, String name) {
    StringBuilder sb = new StringBuilder();
    sb.append(id);
    sb.append("|");
    sb.append(name);
    sb.toString();
    this.id = id;
    this.name = name;
  }

  public void test() {
    try {
      System.out.println(new Object() { }.getClass().getEnclosingClass().getClassLoader());
      System.out.println(Class.forName("SecAgent.utils.ReqLocal").getClassLoader().toString());
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) throws IOException {
    String s1="1", s2 = "2";
    Throwable throwable = new Throwable();
    Object o = null;
    try {
      Thread.currentThread().getContextClassLoader().loadClass("SecAgent.utils.ReqInfo").getDeclaredMethod("putStubData", String.class, Throwable.class, Object.class).invoke(o, s1, throwable, s2);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
  }
}