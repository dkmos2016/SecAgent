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
    super(parent);
//    this.parent = parent;
  }

  @Override
  protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
    System.out.println("AgentClassLoader loading class ");
    System.out.println(this.getParent());
    return super.loadClass(name, resolve);
  }
}
