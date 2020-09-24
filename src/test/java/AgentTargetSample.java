

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class AgentTargetSample<E> {

  public static void main(String[] args) {

  }


  public class DynamicProxy implements InvocationHandler {


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      System.out.println();

      return null;
    }
  }
}
