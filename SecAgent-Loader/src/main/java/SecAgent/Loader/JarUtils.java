package SecAgent.Loader;

import SecAgent.Utils.Conf.Config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import SecAgent.Utils.utils.DefaultLoggerHelper.DefaultLogger;


public class JarUtils {
  private final static String DST_DIR;
  private final static String BASE_DIR;
  private final static DefaultLogger logger = DefaultLogger.getLogger(JarUtils.class, Config.EXCEPTION_PATH);

  static {
    BASE_DIR = "/libs/container";
    DST_DIR = Config.JAR_PATH.substring(0, Config.JAR_PATH.lastIndexOf('/')) + BASE_DIR;

    File file = new File(DST_DIR);
    if (!file.exists()) {
      file.mkdirs();
    }

    if (logger != null) logger.setLevel(DefaultLogger.MyLevel.DEBUG);

  }

  private final String file_path;

  JarUtils(String file_path) {
    this.file_path = file_path;
  }

  public static String doReleaseJar(String file_name, String destination) {
    String dst;
    String src = BASE_DIR + "/" + file_name;

    try {
      InputStream in = JarUtils.class.getResourceAsStream(src);
      if (in == null) {
        logger.error("file not exist in SecAgent.jar! " + src);
        return null;
      }

      FileOutputStream fout;

      dst = DST_DIR + File.separator + (destination == null || destination.isEmpty()?file_name: destination);

      fout = new FileOutputStream(dst);

      int v;
      while ((v = in.read()) != -1) {
        fout.write(v);
      }
      fout.flush();
      fout.close();
      in.close();

    } catch (Exception e) {
      dst = null;
      e.printStackTrace();
      if (logger != null) logger.error(e);
    }

    return dst;
  }

  public boolean Exist(String file_name) {
    return JarUtils.class.getResource(file_name) != null;
  }
}
