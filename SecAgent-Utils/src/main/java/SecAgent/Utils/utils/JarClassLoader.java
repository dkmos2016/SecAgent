package SecAgent.Utils.utils;

import java.net.URL;
import java.net.URLClassLoader;


public class JarClassLoader extends URLClassLoader {


  public JarClassLoader(URL[] urls) {
    super(urls, JarClassLoader.getSystemClassLoader().getParent());
  }

  @Override
  protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
//        return super.loadClass(name, resolve);

    if (name.startsWith("java") || name.startsWith("net") || name.endsWith("ReqInfo") || name.equals("SecAgent.SecAgent_Core.Logger.ExceptionLogger")) {
      return super.loadClass(name, resolve);
    } else {

      Class cls = findClass(name);

      if (resolve) {
        resolveClass(cls);
      }

      return cls;
    }

  }
}
