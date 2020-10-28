package SecAgent.Loader;

import SecAgent.Utils.Conf.Config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;


public class JarUtils {
  private final static String DST_DIR;
  private final static String BASE_DIR;

  static {
    BASE_DIR = "/libs/container";
    DST_DIR = Config.JAR_PATH.substring(0, Config.JAR_PATH.lastIndexOf('/')) + File.separator + "libs";


    File file = new File(DST_DIR);
    if (!file.exists()) {
      file.mkdir();
    }

  }

  private final String file_path;

  JarUtils(String file_path) {
    this.file_path = file_path;
  }

  public static String doReleaseJar(String file_name, String destination) {
    String path = null;
    try {
      System.out.println(BASE_DIR + File.separator + file_name);
      InputStream in = JarUtils.class.getResourceAsStream(BASE_DIR + File.separator + file_name);
      if (in == null) {
        return null;
      }

      FileOutputStream fout;

      if (destination == null || destination.isEmpty()) {
        path = DST_DIR + File.separator + file_name;
      } else {
        path = DST_DIR + File.separator + destination;
      }

      fout = new FileOutputStream(path);


      int v = -1;
      while ((v = in.read()) != -1) {
        fout.write(v);
      }
      fout.flush();
      fout.close();
      in.close();

    } catch (Exception e) {
      path = null;
    }

    return path;
  }

  public boolean Exist(String file_name) {
    return JarUtils.class.getResource(file_name) != null;
  }
}
