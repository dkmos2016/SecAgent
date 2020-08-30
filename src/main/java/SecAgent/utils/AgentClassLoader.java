package SecAgent.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;


public class AgentClassLoader extends ClassLoader {
  protected ClassLoader parent;

  protected AgentClassLoader(ClassLoader parent) {

    System.out.println("use AgentClassLoader.....");
    System.out.println(this.getClass().getClassLoader());
    if (parent != null) {
      this.parent = parent;
      return;
    }

    ClassLoader p = getParent();
    if (p == null) {
      p = getSystemClassLoader();
    }
    this.parent = p;
  }

  @Override
  protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
    System.out.println("AgentClassLoader loading class ");
    return super.loadClass(name, resolve);
  }
}
