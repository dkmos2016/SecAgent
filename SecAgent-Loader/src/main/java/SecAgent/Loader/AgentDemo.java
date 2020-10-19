package SecAgent.Loader;

//import SecAgent.SecAgent_Core.utils.DefaultLoggerHelper.DefaultLogger;
//
//import SecAgent.SecAgent_Core.Conf.Config;
//import SecAgent.SecAgent_Core.SecAsmTransformer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.security.SecureClassLoader;

public class AgentDemo {
//  private static final DefaultLogger logger =
//      DefaultLogger.getLogger(AgentDemo.class, Config.EXCEPTION_PATH);

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
          "com.sun.org.apache.xerces.internal.impl.XMLEntityManager$RewindableInputStream",
          "com.mysql.cj.jdbc.StatementImpl",
          "oracle.jdbc.driver.OracleStatement",
          "oracle.jdbc.driverOracleResultSetImpl",
          "org.apache.tomcat.util.http.fileupload.FileUploadBase",
          "ognl.Ognl"
        };

    TARGET_OTHER_CLASSES =
        new String[] {
          "com.mysql.cj.jdbc.StatementImpl",
        };
  }

  public static void premain(String args, Instrumentation instrumentation)
      throws UnmodifiableClassException, ClassNotFoundException {

    INST = instrumentation;
    try {
//      InputStream stream = AgentDemo.class.getResourceAsStream("libs/SecAgent-1.0.0-SNAPSHOT-BEAT.jar");
      URL url = AgentDemo.class.getProtectionDomain().getCodeSource().getLocation();
      String path = url.toString();
//      System.out.println(path);
      String core_path = path.substring(path.indexOf('/'), path.lastIndexOf('/')) + "/SecAgent-Core-1.0.0-SNAPSHOT-BEAT.jar";


//      System.out.println(AgentDemo.class.getResource(""));

//      String core_path = path.substring(path.indexOf('/'), path.lastIndexOf('!')+1)+"/libs/SecAgent-Core-1.0.0-SNAPSHOT-BEAT.jar";

      File file = new File(core_path);
      FileOutputStream fout = new FileOutputStream(file);
      InputStream in = AgentDemo.class.getResourceAsStream("/libs/SecAgent-Core-1.0.0-SNAPSHOT-BEAT.jar");

      int v = -1;
      while((v=in.read())!=-1) fout.write(v);

      in.close();
      fout.close();

      Class cls = new AgentLoader(new URL[]{file.toURI().toURL()}).loadClass("SecAgent.SecAgent_Core.SecAsmTransformer");
      Constructor constructor = cls.getConstructor();

      Object transformer = constructor.newInstance();
      instrumentation.addTransformer((ClassFileTransformer) transformer, true);
    } catch (Exception e) {
      e.printStackTrace();
    }


    //    instrumentation.retransformClasses(FileOutputStream.class);
    //    instrumentation.retransformClasses(File.class);
    //    instrumentation.retransformClasses(SecurityManager.class);

    TARGET_LOADED_CLASSES = instrumentation.getAllLoadedClasses();

    for (String classname : TARGET_BASIC_CLASSES) {
      try {
        instrumentation.retransformClasses(
            SecureClassLoader.getSystemClassLoader().loadClass(classname));

        //        instrumentation.retransformClasses(
        //               Class.forName(classname));
      } catch (ClassNotFoundException e) {
//        if (logger != null) logger.warn(e);
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

    //    }
  }

  public static void agentmain(String args, Instrumentation instrumentation) {
    //    premain(args, instrumentation);
  }

  @Deprecated
  private static Class getLoadedClass(String classname) {
    for (Class cls : TARGET_LOADED_CLASSES) {
      if (cls.getName().replace("/", ".").equals(classname)) {
        return cls;
      }
    }
    return null;
  }
}
