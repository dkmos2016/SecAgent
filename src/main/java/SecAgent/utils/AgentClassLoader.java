package SecAgent.utils;

import java.net.URL;
import java.net.URLClassLoader;

public class AgentClassLoader extends URLClassLoader {
  protected ClassLoader parent;

  public AgentClassLoader() {
    super(null);
    //    this.parent = null;
  }

  public AgentClassLoader(ClassLoader parent) {
    super(new URL[] {}, parent);
    //    super(parent);
    this.parent = parent;
  }

  @Override
  protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
    return super.loadClass(name, resolve);
  }
}
