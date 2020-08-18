import java.io.File;
import java.io.FileOutputStream;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

public class AgentDemo {
  private static Instrumentation INST;
  private static String[] TARGET_CLASSES;

  static {
    TARGET_CLASSES = new String[] {
      "java.io.FileOutputStream",
            "java.io.File",
            "java.lang.SecurityManager"
    };
  }


  public static void premain(String args, Instrumentation instrumentation) throws UnmodifiableClassException, ClassNotFoundException {
    System.out.println("run in premain");
    INST = instrumentation;

    instrumentation.addTransformer(new SecAsmTransformer(), true);
//    instrumentation.retransformClasses(FileOutputStream.class);
//    instrumentation.retransformClasses(File.class);
//    instrumentation.retransformClasses(SecurityManager.class);
    for (String cls: TARGET_CLASSES) {
      instrumentation.retransformClasses(Class.forName(cls));
    }
  }

  public static void agentmain(String args, Instrumentation instrumentation) {
//    premain(args, instrumentation);
  }
}
