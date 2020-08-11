
import java.lang.instrument.Instrumentation;

public class AgentDemo {
  public static Instrumentation INST;

  public static void premain(String args, Instrumentation instrumentation) {
    System.out.println("run in premain");
    String className = "test";
    INST = instrumentation;

    instrumentation.addTransformer(new SecAsmTransformer());
  }

  public static void agentmain(String args, Instrumentation instrumentation) {
    premain(args, instrumentation);
  }

}
