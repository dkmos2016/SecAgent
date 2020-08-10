import javax.xml.transform.Transformer;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

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
