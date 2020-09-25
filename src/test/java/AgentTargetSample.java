import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class AgentTargetSample<E> {

  public static void main(String[] args) {
    try {
      Class cls = Class.forName("Test");
      Method method = cls.getMethod("print", null);

      Constructor constructor = cls.getConstructor(int.class);
      constructor.newInstance(1);


      Test test = new Test(1);
      Test test1 = null;
      Object obj = test;

      test1 = (Test)obj;


//      method.invoke(null, );
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
