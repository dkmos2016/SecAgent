import SecAgent.utils.DefaultLoggerHelper.DefaultLogger;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.net.URLClassLoader;
import java.security.SecureClassLoader;

public class AgentDemo {
  private static final String[] TARGET_BASIC_CLASSES;
  private static final String[] TARGET_OTHER_CLASSES;
  private static Class[] TARGET_LOADED_CLASSES;
  private static Instrumentation INST;
  private static Class[] classes;

  static {
    TARGET_BASIC_CLASSES =
        new String[] {
          "java.io.FileOutputStream",
          "java.io.FileInputStream",
          //          "java.io.File",
          //          "java.lang.SecurityManager",
          "java.io.ObjectInputStream",
          "java.io.InputStream",
          "java.lang.Runtime",
                "java.lang.ProcessImpl",
//          "java.sql.Statement",
          //              "java.util.ArrayList",
          //          "com.sun.org.apache.xerces.internal.impl.XMLVersionDetector",
          "com.sun.org.apache.xerces.internal.impl.XMLEntityManager",
          "com.mysql.cj.jdbc.StatementImpl",
                "oracle.jdbc.driver.OracleStatement",

        };

    TARGET_OTHER_CLASSES = new String[] {
            "com.mysql.cj.jdbc.StatementImpl",
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

    TARGET_LOADED_CLASSES = instrumentation.getAllLoadedClasses();

    for (String classname : TARGET_BASIC_CLASSES) {
      try{
        instrumentation.retransformClasses(SecureClassLoader.getSystemClassLoader().loadClass(classname));
      } catch (ClassNotFoundException e) {
        DefaultLogger.getLogger(AgentDemo.class).error(e);
      }
    }

//    for (String classname: TARGET_OTHER_CLASSES) {
//      Class cls = null;
//      if ((cls = getLoadedClass(classname)) != null) {
//        instrumentation.retransformClasses(cls);
//      } else {
//        try{
//          cls = SecureClassLoader.getSystemClassLoader().loadClass(classname);
//          instrumentation.retransformClasses(cls);
//        }catch (Exception e) {
//          DefaultLogger.getLogger().error(e);
//        }
//      }
//    }

    //    for (Class cls: instrumentation.getAllLoadedClasses()) {
    //      System.out.println(cls.getName());
    //    }
  }

  public static void agentmain(String args, Instrumentation instrumentation) {
    //    premain(args, instrumentation);
  }

  @Deprecated
  private static Class getLoadedClass(String classname){
    for (Class cls: TARGET_LOADED_CLASSES) {
      if (cls.getName().replace("/", ".").equals(classname)) {
        return cls;
      }
    }
    return null;
  }
}
