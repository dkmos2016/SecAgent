import SecAgent.SecAsm.utils.AsmInvokeOp;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
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

  public static void test(String classname, String methodname, Class[] paramTypes, Object[] params)  {
      try {
        Thread.currentThread().getContextClassLoader().loadClass(classname).getDeclaredMethod(methodname, paramTypes).invoke(null, params);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
//    System.out.println(list);
  }

  public static void main(String[] args) throws IOException, NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException {

    test("java.lang.Math", "floorDiv", new Class[]{int.class, int.class}, new Object[]{10, 2});
  }
}