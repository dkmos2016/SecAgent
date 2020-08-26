


import SecAgent.utils.ReqInfo;
import org.objectweb.asm.Type;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

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

  public static void test( Class []params, int idx)  {
    try{
      Thread.currentThread().getContextClassLoader().loadClass("SecAgent.utils.ReqInfo").getDeclaredMethod("doTest", params );
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) throws IOException {
    test( new Class[]{int.class, int.class, AgentTargetSample.class}, 1);


//    try{
//      Thread.currentThread().getContextClassLoader().loadClass("SecAgent.utils.ReqInfo").getDeclaredMethod("doTest", new Class[]{int.class, int.class, AgentTargetSample.class} );
//    } catch (Exception e) {
//      e.printStackTrace();
//    }


    System.out.println(Type.getType(int.class));
 }
}