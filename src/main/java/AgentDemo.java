import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

public class AgentDemo {
  private static final String[] TARGET_CLASSES;
  private static Instrumentation INST;

  static {
    TARGET_CLASSES =
        new String[] {
          "java.io.FileOutputStream",
          "java.io.FileInputStream",
          //          "java.io.File",
          //          "java.lang.SecurityManager",
          "java.io.ObjectInputStream",
          "java.io.InputStream",
          "java.lang.Runtime",
          "java.sql.Statement",
          //              "java.util.ArrayList",
//          "com.sun.org.apache.xerces.internal.impl.XMLVersionDetector",
          "com.sun.org.apache.xerces.internal.impl.XMLEntityManager",
        };
  }

  public static void premain(String args, Instrumentation instrumentation)
      throws UnmodifiableClassException, ClassNotFoundException {
    System.out.println("run in premain");
    INST = instrumentation;

    instrumentation.addTransformer(new SecAsmTransformer(), true);
    //    instrumentation.retransformClasses(FileOutputStream.class);
    //    instrumentation.retransformClasses(File.class);
    //    instrumentation.retransformClasses(SecurityManager.class);
    for (String cls : TARGET_CLASSES) {
      //      System.out.println(String.format("reload %s", cls));
      instrumentation.retransformClasses(Class.forName(cls));
    }

    //    for (Class cls: instrumentation.getAllLoadedClasses()) {
    //      System.out.println(cls.getName());
    //    }
  }

  public static void agentmain(String args, Instrumentation instrumentation) {
    //    premain(args, instrumentation);
  }
}
