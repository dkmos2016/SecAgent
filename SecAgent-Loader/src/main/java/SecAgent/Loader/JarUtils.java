package SecAgent.Loader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class JarUtils {
  private final String file_path;
  private final static String DIR = JarUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath();

  JarUtils(String file_path) {
    this.file_path = file_path;
  }

  public boolean Exist(String file_name) {
    return JarUtils.class.getResource(file_name) != null;
  }

  public static boolean doReleaseJar(String file_name, String destination) {
    try {
      InputStream in = JarUtils.class.getResourceAsStream(file_name);
      if (in == null) return false;

      FileOutputStream fout;
      if (destination == null || destination.isEmpty()) {
        fout = new FileOutputStream(DIR + File.separator + file_name);
      } else {
        fout = new FileOutputStream(DIR + File.separator + destination);
      }

      int v = -1;
      while((v=in.read())!=-1) {
       fout.write(v);
      }
      fout.flush();
      fout.close();
      in.close();

    } catch (Exception e) {
      return false;
    }

    return true;
  }
}
