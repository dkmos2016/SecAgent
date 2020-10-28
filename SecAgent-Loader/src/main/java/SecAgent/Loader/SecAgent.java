package SecAgent.Loader;

import SecAgent.Utils.Conf.Config;
import SecAgent.Utils.utils.DefaultLoggerHelper.DefaultLogger;
import SecAgent.SecAsm.SecAsmTransformer;

import java.io.FileOutputStream;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.SecureClassLoader;
import java.util.Map;

public class SecAgent {
  private static final DefaultLogger logger =
    DefaultLogger.getLogger(SecAgent.class, Config.EXCEPTION_PATH);

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

    initialize();
  }

  public static void initialize() {
    System.out.println("initialize");
    try {
      /* Override SecAgentInfo.log/SecAgentError.log */
      new FileOutputStream(Config.INFORMATION_PATH);
      new FileOutputStream(Config.EXCEPTION_PATH);

      for (Map.Entry<String, String> entry: Config.CONTAINER_JAR_FILE_NAMEs.entrySet()) {
        String key = entry.getKey();
        String file_name = entry.getValue();

        String path = null;
        if ((path=JarUtils.doReleaseJar(file_name, null)) != null) {
          ;
          // todo addUrl (JarClassLoader)
          /**
           * save unzipped jar's path to CONTAINER_JAR_PATHs
           */
          Config.CONTAINER_JAR_PATHs.put(key, path);
        }
      }
    } catch (Exception e) {
      if (logger != null) logger.error(e);
    }


  }

  public static void premain(String args, Instrumentation instrumentation)
    throws UnmodifiableClassException, ClassNotFoundException {

    INST = instrumentation;

    instrumentation.addTransformer(new SecAsmTransformer(), true);
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
        if (logger != null) logger.warn(e);
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
