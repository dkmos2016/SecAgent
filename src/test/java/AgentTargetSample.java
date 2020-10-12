import java.io.InputStream;
import java.lang.reflect.Method;

public class AgentTargetSample<E> {

  public static void main(String[] args) {
    try {
      Class cls = Class.forName("SecAgent.utils.Common");
      Method method = cls.getMethod("transferTo", InputStream.class);

      method.invoke(null, new Object[] {null});

      //      method.invoke(null, );
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
