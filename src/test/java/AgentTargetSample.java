


import SecAgent.utils.ReqInfo;
import org.objectweb.asm.Type;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class AgentTargetSample<E> {
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

  public static void test( Class []params)  {
    ReqInfo res;
    try{
      res = (ReqInfo) Thread.currentThread().getContextClassLoader().loadClass("SecAgent.utils.ReqInfo").getDeclaredMethod("doTest", params ).invoke(null,1, 2);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    HashMap hashMap = new HashMap();
    ArrayList list = new ArrayList();

  }
}