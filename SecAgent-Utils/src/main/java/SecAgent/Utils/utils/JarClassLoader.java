package SecAgent.Utils.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.util.Arrays;
import java.util.List;

import SecAgent.Utils.Conf.Config;
import SecAgent.Utils.utils.DefaultLoggerHelper.DefaultLogger;

public class JarClassLoader extends URLClassLoader {
  private String baseUrl = null;
  private static final DefaultLogger logger = DefaultLogger.getLogger(JarClassLoader.class, Config.EXCEPTION_PATH);;


  static {
//    baseUrl = JarClassLoader.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    logger.setLevel(DefaultLogger.MyLevel.DEBUG);
  }

  public JarClassLoader() {
    super(new URL[0]);
  }

  public JarClassLoader(ClassLoader parent) {
    super(new URL[0], parent);
  }

  public JarClassLoader(URL[] urls, ClassLoader parent) {
    super(urls, parent);
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public void addURL(String url) {
    URL nurl = null;
    if (url==null || url.isEmpty()) return;

    try {
      File file = new File(url);
      nurl = file.toURI().toURL();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    this.addURL(nurl);
  }

  @Override
  public void addURL(URL url) {
    System.out.println("add url:" + url);
    super.addURL(url);
  }

  @Override
  public URL[] getURLs() {
    return super.getURLs();
  }

  @Override
  public Class<?> findClass(String name) throws ClassNotFoundException {
    return super.findClass(name);
  }


  /**
   *
   * @param name, ex: java/lang/String,...
   * @return
   * @throws ClassNotFoundException
   */
  @Override
  public Class<?> loadClass(String name) throws ClassNotFoundException {
    return super.loadClass(name);
  }

  @Override
  public URL findResource(String name) {
    URL url = null;
    if (baseUrl == null || baseUrl.isEmpty()) {
      try {
        url = new URL(baseUrl + File.separator + name);
      } catch (MalformedURLException e) {
//        e.printStackTrace();
      }
    }
    return url;
  }

  @Override
  public PermissionCollection getPermissions(CodeSource codesource) {
    return super.getPermissions(codesource);
  }
}
