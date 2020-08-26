import SecAgent.SecAsm.utils.AsmInvokeOp;
import SecAgent.utils.ParamsInfo;
import org.apache.log4j.Logger;
import org.objectweb.asm.Type;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
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

  public static void test(Method method, ArrayList params)  {
    if (method != null) {
      try{
        method.invoke(null, params.toArray());
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
    }
//    System.out.println(list);
  }

  public static void main(String[] args) {
    int a = 1;
    if (args == null || args.length == 0) {
    } else {
    }
 }
}