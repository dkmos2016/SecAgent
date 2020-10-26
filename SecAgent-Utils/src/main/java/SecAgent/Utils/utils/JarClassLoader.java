package SecAgent.Utils.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.security.PermissionCollection;

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
    super(null);
  }

  public JarClassLoader(URL[] urls, ClassLoader parent) {
    super(urls, parent);
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public void addURL(String url) {
    URL nurl = null;
    try {
      nurl = new URL(baseUrl + File.separator + url);
    } catch (MalformedURLException e) {

    }
    this.addURL(nurl);
  }

  @Override
  protected void addURL(URL url) {
    super.addURL(url);
  }

  @Override
  public URL[] getURLs() {
    return super.getURLs();
  }

  @Override
  protected Class<?> findClass(String name) throws ClassNotFoundException {
    return super.findClass(name);
  }

  @Override
  protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
    return super.loadClass(name, resolve);
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
  protected PermissionCollection getPermissions(CodeSource codesource) {
    return super.getPermissions(codesource);
  }
}
